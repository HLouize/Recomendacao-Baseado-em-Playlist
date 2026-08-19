package sistemaDeStreaming.recomendacao;

import java.util.List;
import sistemaDeStreaming.classesDeMidia.Midia;
import sistemaDeStreaming.classesDeMidia.Reproduzivel;
import sistemaDeStreaming.organizacao.Playlist;
import sistemaDeStreaming.excecoes.PlaylistVaziaException;
import sistemaDeStreaming.excecoes.CatalogoInsuficienteException;

public interface RecommendationStrategy {

    List<Reproduzivel> recomendar(Playlist playlistBase, List<Midia> catalogoGlobal, int limite)
            throws PlaylistVaziaException, CatalogoInsuficienteException;
}