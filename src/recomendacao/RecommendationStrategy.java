package recomendacao;

import java.util.List;
import classesDeMidia.Midia;
import classesDeMidia.Reproduzivel;
import organizacao.Playlist;
import excecoes.PlaylistVaziaException;
import excecoes.CatalogoInsuficienteException;

public interface RecommendationStrategy {

    List<Reproduzivel> recomendar(Playlist playlistBase, List<Midia> catalogoGlobal, int limite)
            throws PlaylistVaziaException, CatalogoInsuficienteException;
}