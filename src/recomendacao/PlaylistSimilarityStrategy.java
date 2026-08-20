package recomendacao;

import java.util.*;
import classesDeMidia.Midia;
import classesDeMidia.Reproduzivel;
import organizacao.Playlist;
import excecoes.PlaylistVaziaException;
import excecoes.CatalogoInsuficienteException;

public class PlaylistSimilarityStrategy implements RecommendationStrategy {

    @Override
    public List<Reproduzivel> recomendar(Playlist playlistBase, List<Midia> catalogoGlobal, int limite)
            throws PlaylistVaziaException, CatalogoInsuficienteException {

        if (playlistBase == null || playlistBase.getItens().isEmpty()) {
            throw new PlaylistVaziaException("A playlist base está vazia.");
        }

        Map<String, Integer> frequenciaTags = new HashMap<>();
        for (Reproduzivel item : playlistBase.getItens()) {
            if (item.getTags() != null) {
                for (String tag : item.getTags()) {
                    String tagNormalizada = tag.toLowerCase().trim();
                    frequenciaTags.put(tagNormalizada, frequenciaTags.getOrDefault(tagNormalizada, 0) + 1);
                }
            }
        }

        List<Reproduzivel> candidatos = new ArrayList<>();
        Map<Reproduzivel, Integer> pontuacao = new HashMap<>();

        for (Midia midia : catalogoGlobal) {
            if (playlistBase.getItens().contains(midia)) {
                continue;
            }

            if (midia instanceof Reproduzivel) {
                Reproduzivel candidato = (Reproduzivel) midia;
                int score = 0;

                if (candidato.getTags() != null) {
                    for (String tag : candidato.getTags()) {
                        String tagNorm = tag.toLowerCase().trim();
                        if (frequenciaTags.containsKey(tagNorm)) {
                            score += frequenciaTags.get(tagNorm);
                        }
                    }
                }

                if (score > 0) {
                    pontuacao.put(candidato, score);
                    candidatos.add(candidato);
                }
            }
        }

        if (candidatos.isEmpty()) {
            throw new CatalogoInsuficienteException("Nenhuma mídia com perfil semelhante encontrada.");
        }

        candidatos.sort((c1, c2) -> Integer.compare(pontuacao.get(c2), pontuacao.get(c1)));

        if (candidatos.size() > limite) {
            return candidatos.subList(0, limite);
        }

        return candidatos;
    }
}