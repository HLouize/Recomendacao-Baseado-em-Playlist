package principais;

import excecoes.LimiteUsuariosPlanoException;
import planos.PlanoFamily;
import filtros.AdInsertionFilter;
import classesDeMidia.Propaganda;
import classesDeMidia.Reproduzivel;
import classesDeMidia.Faixa;
import java.util.ArrayList;
import java.util.List;
import planos.Gratuito;


public class Main {
    public static void main(String[] args) {
        System.out.println("=== TESTE 1: Limite do Plano Family ===");
        PlanoFamily planoFamily = new PlanoFamily(29.90);

        try {
            // Tentando adicionar 7 usuários (o limite é 6)
            for (int i = 1; i <= 7; i++) {
                Usuario u = new Usuario("User " + i, "user" + i + "@email.com");
                planoFamily.adicionarUsuario(u);
                System.out.println("Usuário " + i + " adicionado com sucesso.");
            }
        } catch (LimiteUsuariosPlanoException e) {
            System.err.println("Exceção capturada com sucesso: " + e.getMessage());
        }

        System.out.println("\n=== TESTE 2: Filtro de Propagandas ===");
        Usuario userGratuito = new Usuario("João", "joao@email.com");
        userGratuito.setPlano(new Gratuito());// Retorna exibirPropaganda() == true

        Propaganda vinheta = new Propaganda("Compre Premium!", 15);
        AdInsertionFilter filtroAds = new AdInsertionFilter(2, vinheta); // Insere a cada 2 mídias

        // Simulando uma lista de recomendações que veio da estratégia
        List<Reproduzivel> recomendacoes = new ArrayList<>();
        recomendacoes.add(new Faixa("Música 1", 3.5, 2023, "Artista A"));
        recomendacoes.add(new Faixa("Música 2", 4.0, 2023, "Artista B"));
        recomendacoes.add(new Faixa("Música 3", 2.5, 2023, "Artista C"));

        List<Reproduzivel> resultado = filtroAds.filtrar(recomendacoes, userGratuito);

        System.out.println("Tamanho da lista com propagandas: " + resultado.size());
        for (Reproduzivel item : resultado) {
            item.play();
        }
    }
}