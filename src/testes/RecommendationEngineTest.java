package testes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import classesDeMidia.Faixa;
import classesDeMidia.Midia;
import classesDeMidia.Propaganda;
import excecoes.LimiteUsuariosPlanoException;
import excecoes.PlaylistVaziaException;
import organizacao.Playlist;
import planos.Gratuito;
import planos.PlanoFamily;
import planos.PlanoIndividual;
import principais.RecommendationEngine;
import principais.Usuario;
import recomendacao.PlaylistSimilarityStrategy;
import filtros.AdInsertionFilter;
import filtros.AntiRepetitionFilter;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendationEngineTest {

    private List<Midia> catalogo;
    private Usuario usuarioGratuito;
    private Usuario usuarioIndividual;
    private Propaganda adPadrao;

    @BeforeEach
    void setUp() {
        catalogo = new ArrayList<>();
        adPadrao = new Propaganda("Anúncio Exemplo", 30);
        usuarioGratuito = new Usuario("Lucas", "lucas@email.com", new Gratuito());
        usuarioIndividual = new Usuario("Ana", "ana@email.com", new PlanoIndividual(29.90));
    }

    @Test
    @DisplayName("Teste 1: Priorização por semelhança e remoção de duplicados na PlaylistSimilarityStrategy")
    void testPlaylistSimilarityStrategy() throws Exception {
        Playlist playlistBase = new Playlist("Minha Vibe");

        Faixa faixaBase = new Faixa("Song 1", 3.5, 2023, "Artista A");
        faixaBase.adicionarTag("Pop");
        playlistBase.adicionarItem(faixaBase);

        Faixa faixaSemelhante = new Faixa("Song 2", 4.0, 2023, "Artista B");
        faixaSemelhante.adicionarTag("Pop");

        Faixa faixaDiferente = new Faixa("Song 3", 2.5, 2021, "Artista C");
        faixaDiferente.adicionarTag("Jazz");

        catalogo.add(faixaBase);
        catalogo.add(faixaSemelhante);
        catalogo.add(faixaDiferente);

        RecommendationEngine engine = new RecommendationEngine(new PlaylistSimilarityStrategy());
        Playlist recomendada = engine.gerarPlaylistRecomendada("Descobertas", playlistBase, usuarioIndividual, catalogo, 1);

        assertTrue(recomendada.getItens().contains(faixaSemelhante));
        assertFalse(recomendada.getItens().contains(faixaBase));
    }

    @Test
    @DisplayName("Teste 2: Funcionamento do AntiRepetitionFilter com o histórico do usuário")
    void testAntiRepetitionFilter() throws Exception {
        Playlist playlistBase = new Playlist("Base");
        Faixa f1 = new Faixa("Musica 1", 3.0, 2020, "Banda 1");
        f1.adicionarTag("Rock");
        playlistBase.adicionarItem(f1);

        Faixa f2 = new Faixa("Musica 2", 3.2, 2021, "Banda 2");
        f2.adicionarTag("Rock");

        Faixa f3 = new Faixa("Musica 3", 3.5, 2021, "Banda 3");
        f3.adicionarTag("Rock");

        catalogo.add(f2);
        catalogo.add(f3);

        usuarioIndividual.acessarMidia(f2);

        RecommendationEngine engine = new RecommendationEngine(new PlaylistSimilarityStrategy());
        engine.adicionarFiltro(new AntiRepetitionFilter());

        Playlist result = engine.gerarPlaylistRecomendada("Filtro Repetição", playlistBase, usuarioIndividual, catalogo, 2);

        assertFalse(result.getItens().contains(f2));
        assertTrue(result.getItens().contains(f3));
    }

    @Test
    @DisplayName("Teste 3: AdInsertionFilter (com anúncios no plano Gratuito vs sem anúncios no Individual)")
    void testAdInsertionFilter() throws Exception {
        Playlist playlistBase = new Playlist("Festa");
        Faixa baseTrack = new Faixa("M1", 3.0, 2020, "Artista Pop");
        baseTrack.adicionarTag("Pop");
        playlistBase.adicionarItem(baseTrack);

        for (int i = 0; i < 4; i++) {
            Faixa f = new Faixa("Pop " + i, 3.0, 2020, "Vários");
            f.adicionarTag("Pop");
            catalogo.add(f);
        }

        RecommendationEngine engine = new RecommendationEngine(new PlaylistSimilarityStrategy());
        engine.adicionarFiltro(new AdInsertionFilter(2, adPadrao));

        Playlist recGratuito = engine.gerarPlaylistRecomendada("Rec Free", playlistBase, usuarioGratuito, catalogo, 4);
        assertTrue(recGratuito.getItens().contains(adPadrao));

        Playlist recIndividual = engine.gerarPlaylistRecomendada("Rec Premium", playlistBase, usuarioIndividual, catalogo, 4);
        assertFalse(recIndividual.getItens().contains(adPadrao));
    }

    @Test
    @DisplayName("Teste 4: Asserção do disparo de PlaylistVaziaException com assertThrows")
    void testPlaylistVaziaException() {
        Playlist playlistVazia = new Playlist("Vazia");
        RecommendationEngine engine = new RecommendationEngine(new PlaylistSimilarityStrategy());

        assertThrows(PlaylistVaziaException.class, () -> {
            engine.gerarPlaylistRecomendada("Resultado", playlistVazia, usuarioIndividual, catalogo, 5);
        });
    }

    @Test
    @DisplayName("Teste 5: Disparo de LimiteUsuariosPlanoException ao inserir o 7º usuário no PlanoFamily")
    void testLimitePlanoFamilia() {
        PlanoFamily planoFamily = new PlanoFamily(39.90);

        // Preenche até o limite de 6 membros
        for (int i = 1; i <= 6; i++) {
            planoFamily.adicionarUsuario(new Usuario("Membro " + i, "email" + i + "@teste.com", planoFamily));
        }

        // Tenta adicionar o 7º membro e espera a exceção
        Usuario setimoMembro = new Usuario("Membro Extra", "extra@teste.com", planoFamily);
        assertThrows(LimiteUsuariosPlanoException.class, () -> {
            planoFamily.adicionarUsuario(setimoMembro);
        });
    }
}