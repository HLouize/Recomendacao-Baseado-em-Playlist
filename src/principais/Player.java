package principais;

import classesDeMidia.Faixa;
import classesDeMidia.Midia;
import classesDeMidia.Propaganda;
import classesDeMidia.Reproduzivel;
import excecoes.CatalogoInsuficienteException;
import excecoes.PlaylistVaziaException;
import filtros.AdInsertionFilter;
import filtros.AntiRepetitionFilter;
import organizacao.Playlist;
import recomendacao.PlaylistAutoCompleteStrategy;

import java.util.List;

public class Player {
    private boolean emReproducao;
    private int contadorPropaganda;

    public Player() {
        this.emReproducao = false;
        this.contadorPropaganda = 0;
    }

    public void iniciarReproducao(Usuario usuario, Playlist playlist, List<Midia> catalogoGlobal) {
        if (playlist == null || playlist.getItens().isEmpty()) {
            System.out.println("[AVISO] A playlist selecionada esta vazia!");
            return;
        }

        this.emReproducao = true;
        System.out.println("\n[PLAY] Iniciando reproducao da playlist: " + playlist.getNome());

        for (Reproduzivel item : playlist.getItens()) {
            if (!emReproducao) break;
            tocarItem(usuario, item);
        }

        if (emReproducao) {
            System.out.println("\n[AUTOPLAY] As musicas da playlist acabaram. Iniciando recomendacao automatica...");
            try {
                RecommendationEngine engine = new RecommendationEngine(new PlaylistAutoCompleteStrategy());
                engine.adicionarFiltro(new AntiRepetitionFilter());

                Playlist recomendadas = engine.gerarPlaylistRecomendada(
                        "Autoplay - " + playlist.getNome(),
                        playlist,
                        usuario,
                        catalogoGlobal,
                        5
                );

                for (Reproduzivel item : recomendadas.getItens()) {
                    if (!emReproducao) break;
                    tocarItem(usuario, item);
                }

            } catch (PlaylistVaziaException | CatalogoInsuficienteException e) {
                System.out.println("[AVISO] Nao foi possivel carregar mais recomendacoes: " + e.getMessage());
            }
        }

        this.emReproducao = false;
        System.out.println("[STOP] Reproducao finalizada.");
    }

    private void tocarItem(Usuario usuario, Reproduzivel item) {
        if (usuario.getPlano() != null && usuario.getPlano().exibirPropaganda()) {
            contadorPropaganda++;
            if (contadorPropaganda % 2 == 0) {
                Propaganda ad = new Propaganda("Anuncio Patrocinado", 15, "Patrocinador Oficial");
                System.out.println("[ANUNCIO]: " + ad.getNome() + " (Patrocinado por: " + ad.getAnunciante() + ")");
                pausa(1000);
            }
        }

        if (item instanceof Midia m) {
            System.out.println("Tocando: " + m.getNome());
        }
        usuario.adicionarAoHistorico(item);
        pausa(1500);
    }

    public void parar() {
        this.emReproducao = false;
        System.out.println("[PAUSA] Reproducao interrompida pelo usuario.");
    }

    private void pausa(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
