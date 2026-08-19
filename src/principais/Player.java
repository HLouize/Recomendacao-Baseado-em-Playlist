package principais;

import classesDeMidia.Midia;
import classesDeMidia.Propaganda;
import classesDeMidia.Reproduzivel;
import excecoes.CatalogoInsuficienteException;
import excecoes.PlaylistVaziaException;
import filtros.AdInsertionFilter;
import filtros.AntiRepetitionFilter;
import organizacao.Playlist;
import recomendacao.PlaylistSimilarityStrategy;

import java.util.List;

public class Player {
    private boolean emReproducao;
    private int contadorPropagandaAutoplay;

    public Player() {
        this.emReproducao = false;
        this.contadorPropagandaAutoplay = 0;
    }

    public void iniciarReproducao(Usuario usuario, Playlist playlist, List<Midia> catalogoGlobal) {
        if (playlist == null || playlist.getItens().isEmpty()) {
            System.out.println("[AVISO] A playlist selecionada esta vazia!");
            return;
        }

        this.emReproducao = true;
        System.out.println("\n[PLAY] Iniciando reproducao da playlist: " + playlist.getNome());

        // 1. Aplica o filtro de anuncios original se for plano gratuito
        Playlist playlistParaTocar = playlist;
        if (usuario.getPlano() != null && usuario.getPlano().exibirPropaganda()) {
            AdInsertionFilter adFilter = new AdInsertionFilter();
            playlistParaTocar = adFilter.filtrar(playlist, usuario, catalogoGlobal);
        }

        // 2. Toca a playlist do usuario
        for (Reproduzivel item : playlistParaTocar.getItens()) {
            if (!emReproducao) break;
            tocarItem(usuario, item);
        }

        // 3. Autoplay (Recomendacao) baseada estritamente na ULTIMA musica original
        if (emReproducao && !playlist.getItens().isEmpty()) {
            System.out.println("\n[AUTOPLAY] A playlist acabou. Buscando recomendacoes baseadas na ultima musica...");

            // Pega a ultima musica da playlist original (ignorando os anuncios inseridos)
            Reproduzivel ultimaMusica = playlist.getItens().get(playlist.getItens().size() - 1);
            
            // Cria uma playlist temporaria apenas com a ultima musica para forcar a tag (ex: Rock)
            Playlist baseAutoplay = new Playlist("Base Autoplay");
            baseAutoplay.adicionarItem(ultimaMusica);

            try {
                // Usa a estrategia de similaridade que avalia as Tags
                RecommendationEngine engine = new RecommendationEngine(new PlaylistSimilarityStrategy());
                engine.adicionarFiltro(new AntiRepetitionFilter());

                Playlist recomendadas = engine.gerarPlaylistRecomendada(
                        "Autoplay Recomendado",
                        baseAutoplay,
                        usuario,
                        catalogoGlobal,
                        5
                );

                for (Reproduzivel item : recomendadas.getItens()) {
                    if (!emReproducao) break;
                    
                    // Insercao manual de anuncio durante o Autoplay para plano gratuito
                    if (usuario.getPlano() != null && usuario.getPlano().exibirPropaganda()) {
                        contadorPropagandaAutoplay++;
                        if (contadorPropagandaAutoplay % 2 != 0) {
                            Propaganda ad = new Propaganda("Anuncio Premium", 15, "Patrocinador Autoplay");
                            System.out.println("\n[ANUNCIO]: " + ad.getNome() + " - " + ad.getAnunciante());
                            pausa(1500);
                        }
                    }

                    tocarItem(usuario, item);
                }

            } catch (PlaylistVaziaException | CatalogoInsuficienteException e) {
                System.out.println("[AVISO] Nao foi possivel carregar mais recomendacoes: " + e.getMessage());
            }
        }

        this.emReproducao = false;
        System.out.println("\n[STOP] Reproducao finalizada.");
    }

    private void tocarItem(Usuario usuario, Reproduzivel item) {
        if (item instanceof Midia m) {
            System.out.println(">> Tocando agora: " + m.getNome());
        } else {
            System.out.println(">> Tocando agora: Item Reproduzivel");
        }
        usuario.adicionarAoHistorico(item);
        
        // Simula o tempo da musica rodando no terminal
        pausa(2000); 
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
