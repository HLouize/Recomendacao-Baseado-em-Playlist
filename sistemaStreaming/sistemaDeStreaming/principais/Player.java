package principais;

// Imports de Midia
import classesDeMidia.Reproduzivel;
import classesDeMidia.Faixa;
import classesDeMidia.Episodio;

// Imports de Organizacao
import organizacao.Album;
import organizacao.Playlist;
import organizacao.Podcast;

public class Player {
    public void tocarItem(Reproduzivel item) { item.play(); }
    public void pausarItem(Reproduzivel item) { item.pause(); }
    public void pararItem(Reproduzivel item) { item.stop(); }

    public void executarPlaylist(Playlist playlist) {
        System.out.println("\n=== Iniciando Playlist: " + playlist.getNome() + " ===");
        for (Reproduzivel item : playlist.getItens()) {
            tocarItem(item);
        }
    }

    public void executarAlbum(Album album) {
        System.out.println("\n=== Tocando Álbum Completo: " + album.getNome() + " ===");
        for (Faixa faixa : album.getFaixas()) {
            tocarItem(faixa);
        }
    }

    public void executarPodcast(Podcast podcast) {
        System.out.println("\n=== Tocando Podcast: " + podcast.getNome() + " ===");
        for (Episodio ep : podcast.getEpisodios()) {
            tocarItem(ep);
        }
    }
}