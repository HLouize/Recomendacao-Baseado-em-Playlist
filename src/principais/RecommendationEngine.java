package principais;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import classesDeMidia.Midia;
import classesDeMidia.Playlist;
import classesDeMidia.Reproduzivel;
import filtros.RecommendationsFilter;
import estrategias.RecommendationStrategy;
import organizacao.Usuario;

/**
 * Gerencia e executa o fluxo completo do motor de recomendação.
 * <p>
 * Aplica a estratégia configurada para gerar recomendações iniciais e
 * estende o resultado encadeando uma sequência de filtros cadastrados.
 * </p>
 */
public class RecommendationEngine {

    private RecommendationStrategy estrategiaAtiva;
    private final List<RecommendationsFilter> filtros;

    /**
     * Construtor do motor de recomendação.
     * 
     * @param estrategiaInicial Estratégia de recomendação padrão a ser adotada.
     * @throws NullPointerException se {@code estrategiaInicial} for nula.
     */
    public RecommendationEngine(RecommendationStrategy estrategiaInicial) {
        this.estrategiaAtiva = Objects.requireNonNull(estrategiaInicial, "A estratégia inicial não pode ser nula.");
        this.filtros = new ArrayList<>();
    }

    /**
     * Altera a estratégia de recomendação em tempo de execução.
     * 
     * @param novaEstrategia Nova estratégia a ser adotada.
     */
    public void setEstrategia(RecommendationStrategy novaEstrategia) {
        if (novaEstrategia != null) {
            this.estrategiaAtiva = novaEstrategia;
        }
    }

    /**
     * Adiciona um novo filtro à sequência de pós-processamento.
     * 
     * @param filtro Instância de {@link RecommendationsFilter} a ser encadeada.
     */
    public void adicionarFiltro(RecommendationsFilter filtro) {
        if (filtro != null) {
            this.filtros.add(filtro);
        }
    }

    /**
     * Gerencia a geração de uma nova playlist recomendada.
     * 
     * @param nomeNovaPlaylist Nome que será atribuído à nova {@link Playlist} gerada.
     * @param playlistBase     Playlist de referência utilizada para a recomendação.
     * @param usuario          Usuário que receberá as recomendações (utilizado pelos filtros).
     * @param catalogoGlobal   Lista completa de mídias disponíveis no sistema.
     * @param limite           Quantidade máxima de mídias recomendadas.
     * @return Uma nova {@link Playlist} contendo as mídias processadas pelos filtros.
     */
    public Playlist gerarPlaylistRecomendada(String nomeNovaPlaylist, Playlist playlistBase, Usuario usuario, List<Midia> catalogoGlobal, int limite) {
        // 1. Obtém a lista inicial da estratégia
        List<Reproduzivel> recomendacoes = estrategiaAtiva.recomendar(playlistBase, catalogoGlobal, limite);

        if (recomendacoes == null) {
            recomendacoes = new ArrayList<>();
        }

        // 2. Aplica sequencialmente cada filtro cadastrado
        for (RecommendationsFilter filtro : filtros) {
            recomendacoes = filtro.filtrar(recomendacoes, usuario);
            if (recomendacoes == null) {
                recomendacoes = new ArrayList<>();
                break;
            }
        }

        // 3. Instancia e constrói a nova Playlist
        Playlist playlistResultante = new Playlist(nomeNovaPlaylist);
        for (Reproduzivel item : recomendacoes) {
            if (item != null) {
                playlistResultante.adicionarMidia(item);
            }
        }

        return playlistResultante;
    }
}
