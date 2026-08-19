package principais;

import classesDeMidia.Midia;
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

    public Player() {
        this.emReproducao = false;
    }

    public void iniciarReproducao(Usuario usuario, Playlist playlist, List<Midia> catalogoGlobal) {
        if (playlist == null || playlist.getItens().isEmpty()) {
            System.out.println("[AVISO] A playlist selecionada esta vazia!");
            return;
        }

        this.emReproducao = true;
        System.out.println("\n[PLAY] Iniciando reproducao da playlist: " + playlist.getNome());

        // Aplica o filtro de anuncios para plano Gratuito
        Playlist playlistParaTocar = playlist;
        if (usuario.getPlano() != null && usuario.getPlano().exibirPropaganda()) {
            AdInsertionFilter adFilter = new AdInsertionFilter();
            playlistParaTocar = adFilter.filtrar(playlist, usuario, catalogoGlobal);
        }

        for (Reproduzivel item : playlistParaTocar.getItens()) {
            if (!emReproducao) break;
            tocarItem(usuario, item);
        }

        // Recomendacao automatica ao fim da playlist (Autoplay)
        if (emReproducao) {
            System.out.println("\n[AUTOPLAY] As musicas da playlist acabaram. Gerando recomendacoes...");
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
        if (item instanceof Midia m) {
            System.out.println("Tocando: " + m.getNome());
        } else {
            System.out.println("Tocando: " + item.getTitulo());
        }
        usuario.adicionarAoHistorico(item);
        pausa(1200);
    }

    public void parar() {
        this.emReproducao = false;
        System.out.println("[PAUSA] Reproducao interrompida.");
    }

    private void pausa(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
