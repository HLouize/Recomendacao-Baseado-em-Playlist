package filtros;

import java.util.ArrayList;
import java.util.List;
import classesDeMidia.Reproduzivel;
import principais.Usuario;

/**
 * Filtro de pós-processamento responsável por evitar repetições de mídias no motor de recomendação.
 * <p>
 * Consulta o histórico de reprodução do usuário e remove da lista de recomendações
 * as mídias que já foram executadas recentemente pelo player.
 * </p>
 */
public class AntiRepetitionFilter implements RecommendationsFilter {

    /**
     * Filtra a lista de recomendações removendo qualquer mídia presente no histórico do usuário.
     *
     * @param recomendacoes Lista original de recomendações.
     * @param usuario Usuário destinatário das recomendações.
     * @return Lista filtrada contendo apenas mídias que o usuário não ouviu recentemente.
     */
    @Override
    public List<Reproduzivel> filtrarRecomendacoes(List<Reproduzivel> recomendacoes, Usuario usuario) {
        if (recomendacoes == null || usuario == null) {
            return recomendacoes;
        }

        List<Reproduzivel> recomendacoesFiltradas = new ArrayList<>();

        // Obtém o histórico de mídias reproduzidas do usuário
        List<Reproduzivel> historico = usuario.getHistorico();

        for (Reproduzivel media : recomendacoes) {
            // Adiciona na lista final apenas se NÃO estiver presente no histórico recente
            if (historico == null || !historico.contains(media)) {
                recomendacoesFiltradas.add(media);
            }
        }

        return recomendacoesFiltradas;
    }
}
