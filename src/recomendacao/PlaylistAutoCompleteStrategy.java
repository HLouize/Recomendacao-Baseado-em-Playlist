package recomendacao;

import java.util.ArrayList;
import java.util.List;
import classesDeMidia.Faixa;
import classesDeMidia.Episodio;
import classesDeMidia.Midia;
import classesDeMidia.Reproduzivel;
import organizacao.Playlist;
import excecoes.PlaylistVaziaException;
import excecoes.CatalogoInsuficienteException;

public class PlaylistAutoCompleteStrategy implements RecommendationStrategy {

    @Override
    public List<Reproduzivel> recomendar(Playlist playlistBase, List<Midia> catalogoGlobal, int limite)
            throws PlaylistVaziaException, CatalogoInsuficienteException {

        if (playlistBase == null || playlistBase.getItens().isEmpty()) {
            throw new PlaylistVaziaException("A playlist base está vazia para autocompletar a reprodução.");
        }

        List<Reproduzivel> itens = playlistBase.getItens();
        Reproduzivel ultimoItem = itens.get(itens.size() - 1);

        List<Reproduzivel> recomendados = new ArrayList<>();

        for (Midia midia : catalogoGlobal) {
            if (playlistBase.getItens().contains(midia)) {
                continue;
            }

            if (ultimoItem instanceof Faixa && midia instanceof Faixa) {
                Faixa ultimaFaixa = (Faixa) ultimoItem;
                Faixa faixaCatalogo = (Faixa) midia;

                if (faixaCatalogo.getArtista().equalsIgnoreCase(ultimaFaixa.getArtista()) ||
                    (faixaCatalogo.getAlbum() != null && faixaCatalogo.getAlbum().equals(ultimaFaixa.getAlbum()))) {
                    recomendados.add(faixaCatalogo);
                }
            } 
            else if (ultimoItem instanceof Episodio && midia instanceof Episodio) {
                Episodio ultimoEp = (Episodio) ultimoItem;
                Episodio epCatalogo = (Episodio) midia;

                if (epCatalogo.getPodcast().equals(ultimoEp.getPodcast()) ||
                    epCatalogo.getPodcast().getAssunto().equalsIgnoreCase(ultimoEp.getPodcast().getAssunto())) {
                    recomendados.add(epCatalogo);
                }
            }
        }

        if (recomendados.isEmpty()) {
            throw new CatalogoInsuficienteException("Não foram encontradas mídias do mesmo contexto imediato.");
        }

        return recomendados.subList(0, Math.min(limite, recomendados.size()));
    }
}