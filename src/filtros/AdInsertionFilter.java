package filtros;

import java.util.ArrayList;
import java.util.List;
import classesDeMidia.Reproduzivel;
import classesDeMidia.Propaganda; // Altere para a classe real de vinheta/propaganda do seu projeto
import principais.Usuario;

/**
 * Filtro de pós-processamento responsável pela integração com o plano do usuário.
 * <p>
 * Caso o usuário pertença ao plano Gratuito (cujo método {@code exibirPropaganda()} retorna {@code true}),
 * o filtro insere periodicamente uma mídia do tipo propaganda/vinheta a cada N itens recomendados.
 * Se o usuário for de plano pago, a lista de recomendações permanece inalterada.
 * </p>
 */
public class AdInsertionFilter implements RecommendationsFilter {

    private final int intervaloItens;
    private final Propaganda mídiaPropaganda;

    /**
     * Construtor do filtro de inserção de anúncios.
     *
     * @param intervaloItens Número de mídias normais recomendadas antes de inserir um anúncio (ex: a cada 3 mídias).
     * @param mídiaPropaganda Objeto contendo a vinheta/propaganda que será inserida.
     */
    public AdInsertionFilter(int intervaloItens, Propaganda mídiaPropaganda) {
        this.intervaloItens = intervaloItens;
        this.mídiaPropaganda = mídiaPropaganda;
    }

    /**
     * Aplica a lógica de inserção de anúncios se o plano do usuário solicitar a exibição de propagandas.
     *
     * @param recomendacoes Lista original de mídias recomendadas.
     * @param usuario Usuário destinatário das recomendações.
     * @return Lista tratada com ou sem anúncios inseridos periodicamente.
     */
    @Override
    public List<Reproduzivel> filtrarRecomendacoes(List<Reproduzivel> recomendacoes, Usuario usuario) {
        // Validação básica e verificação do plano do usuário
        if (recomendacoes == null || usuario == null || usuario.getPlano() == null) {
            return recomendacoes;
        }

        // Se o plano NÃO exibir propagandas (usuário Premium/Pago), retorna a lista sem modificações
        if (!usuario.getPlano().exibirPropaganda()) {
            return recomendacoes;
        }

        List<Reproduzivel> resultadoComPropagandas = new ArrayList<>();
        int contador = 0;

        for (Reproduzivel media : recomendacoes) {
            resultadoComPropagandas.add(media);
            contador++;

            // A cada 'intervaloItens' adicionados, insere a propaganda
            if (contador % intervaloItens == 0 && mídiaPropaganda != null) {
                resultadoComPropagandas.add(mídiaPropaganda);
            }
        }

        return resultadoComPropagandas;
    }
}
