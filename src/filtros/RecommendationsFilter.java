package filtros;

import java.util.List;
import classesDeMidia.Reproduzivel;
import principais.Usuario;

/**
 * Interface que define o contrato para os filtros de pós-processamento
 * aplicados sobre a lista de mídias recomendadas antes da entrega ao usuário.
 */
public interface RecommendationsFilter {

    /**
     * Filtra ou modifica a lista de recomendações gerada por uma estratégia de recomendação.
     *
     * @param recomendacoes Lista de mídias originalmente recomendadas.
     * @param usuario Usuário para o qual a recomendação está sendo gerada.
     * @return Lista de mídias filtrada/processada.
     */
    List<Reproduzivel> filtrarRecomendacoes(List<Reproduzivel> recomendacoes, Usuario usuario);
}
