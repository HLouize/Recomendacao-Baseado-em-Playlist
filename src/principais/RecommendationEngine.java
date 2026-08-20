package principais;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import classesDeMidia.Midia;
import organizacao.Playlist;
import classesDeMidia.Reproduzivel;
import filtros.RecommendationsFilter;
import recomendacao.RecommendationStrategy;
import excecoes.PlaylistVaziaException;
import excecoes.CatalogoInsuficienteException;

/**
 * Gerencia e executa o fluxo completo do motor de recomendação.
 * Aplica a estratégia configurada para gerar recomendações iniciais e
 * estende o resultado encadeando uma sequência de filtros cadastrados.
 */
public class RecommendationEngine {

    private RecommendationStrategy estrategiaAtiva;
    private final List<RecommendationsFilter> filtros;

    public RecommendationEngine(RecommendationStrategy estrategiaInicial) {
        this.estrategiaAtiva = Objects.requireNonNull(estrategiaInicial, "A estratégia inicial não pode ser nula.");
        this.filtros = new ArrayList<>();
    }

    public void setEstrategia(RecommendationStrategy novaEstrategia) {
        if (novaEstrategia != null) {
            this.estrategiaAtiva = novaEstrategia;
        }
    }

    public void adicionarFiltro(RecommendationsFilter filtro) {
        if (filtro != null) {
            this.filtros.add(filtro);
        }
    }

    public Playlist gerarPlaylistRecomendada(String nomeNovaPlaylist, Playlist playlistBase, Usuario usuario, List<Midia> catalogoGlobal, int limite) {

        // regra da Playlist vazia
        if (playlistBase == null || playlistBase.getItens().isEmpty()) {
            throw new PlaylistVaziaException("A playlist base está vazia. Não é possível gerar recomendações.");
        }

        List<Reproduzivel> recomendacoes = estrategiaAtiva.recomendar(playlistBase, catalogoGlobal, limite);

        if (recomendacoes == null) {
            recomendacoes = new ArrayList<>();
        }

        // regra do catálogo insuficiente
        if (recomendacoes.isEmpty() || recomendacoes.size() < limite) {
            throw new CatalogoInsuficienteException("O catálogo não possui itens compatíveis suficientes para preencher a recomendação.");
        }

        for (RecommendationsFilter filtro : filtros) {
            recomendacoes = filtro.filtrar(recomendacoes, usuario);
            if (recomendacoes == null) {
                recomendacoes = new ArrayList<>();
                break;
            }
        }

        Playlist playlistResultante = new Playlist(nomeNovaPlaylist);
        for (Reproduzivel item : recomendacoes) {
            if (item != null) {
                playlistResultante.adicionarItem(item);
            }
        }

        return playlistResultante;
    }
}