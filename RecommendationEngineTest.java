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
        adPadrao = new Propaganda("Anúncio Exemplo");
        usuarioGratuito = new Usuario("Lucas", new Gratuito());
        usuarioIndividual = new Usuario("Ana", new PlanoIndividual());
    }

    @Test
    @DisplayName("Teste 1: Priorização por semelhança e remoção de duplicados na PlaylistSimilarityStrategy")
    void testPlaylistSimilarityStrategy() throws Exception {
        Playlist playlistBase = new Playlist("Minha Vibe");
        Faixa faixaBase = new Faixa("Song 1", "Artista A", "Pop");
        playlistBase.adicionarItem(faixaBase);

        Faixa faixaSemelhante = new Faixa("Song 2", "Artista B", "Pop");
        Faixa faixaDiferente = new Faixa("Song 3", "Artista C", "Jazz");

        catalogo.add(faixaBase);
        catalogo.add(faixaSemelhante);
        catalogo.add(faixaDiferente);

        RecommendationEngine engine = new RecommendationEngine(new PlaylistSimilarityStrategy());
        Playlist recomendada = engine.gerarPlaylistRecomendada("Descobertas", playlistBase, usuarioIndividual, catalogo, 5);

        assertTrue(recomendada.getItens().contains(faixaSemelhante));
        assertFalse(recomendada.getItens().contains(faixaBase));
    }

    @Test
    @DisplayName("Teste 2: Funcionamento do AntiRepetitionFilter com o histórico do usuário")
    void testAntiRepetitionFilter() throws Exception {
        Playlist playlistBase = new Playlist("Base");
        Faixa f1 = new Faixa("Musica 1", "Rock");
        playlistBase.adicionarItem(f1);

        Faixa f2 = new Faixa("Musica 2", "Rock");
        Faixa f3 = new Faixa("Musica 3", "Rock");

        catalogo.add(f2);
        catalogo.add(f3);

        usuarioIndividual.adicionarAoHistorico(f2);

        RecommendationEngine engine = new RecommendationEngine(new PlaylistSimilarityStrategy());
        engine.adicionarFiltro(new AntiRepetitionFilter());

        Playlist result = engine.gerarPlaylistRecomendada("Filtro Repetição", playlistBase, usuarioIndividual, catalogo, 5);

        assertFalse(result.getItens().contains(f2));
        assertTrue(result.getItens().contains(f3));
    }

    @Test
    @DisplayName("Teste 3: AdInsertionFilter (com anúncios no plano Gratuito vs sem anúncios no Individual)")
    void testAdInsertionFilter() throws Exception {
        Playlist playlistBase = new Playlist("Festa");
        playlistBase.adicionarItem(new Faixa("M1", "Pop"));

        for (int i = 0; i < 4; i++) {
            catalogo.add(new Faixa("Pop " + i, "Pop"));
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
    void testLimitePlanoFamilia() throws LimiteUsuariosPlanoException {
        PlanoFamily planoFamily = new PlanoFamily();

        for (int i = 1; i <= 6; i++) {
            planoFamily.adicionarMembro(new Usuario("Membro " + i, planoFamily));
        }

        assertEquals(6, planoFamily.getQuantidadeMembros());

        Usuario setimoMembro = new Usuario("Membro Extra", planoFamily);
        assertThrows(LimiteUsuariosPlanoException.class, () -> {
            planoFamily.adicionarMembro(setimoMembro);
        });
    }
}