package filtros;

import java.util.List;
import classesDeMidia.Reproduzivel;
import principais.Usuario;

/**
 * Interface que define o contrato para os filtros de pós-processamento
 * aplicados sobre a lista de mídias recomendadas antes da entrega ao usuário.
 */
public interface RecommendationsFilter {
    List<Reproduzivel> filtrar(List<Reproduzivel> recomendacoes, Usuario usuario);
}
