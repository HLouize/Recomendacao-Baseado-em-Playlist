package sistemaDeStreaming.recomendacao;

import java.util.*;
import sistemaDeStreaming.classesDeMidia.Midia;
import sistemaDeStreaming.classesDeMidia.Reproduzivel;
import sistemaDeStreaming.organizacao.Playlist;
import sistemaDeStreaming.excecoes.PlaylistVaziaException;
import sistemaDeStreaming.excecoes.CatalogoInsuficienteException;

public class PlaylistSimilarityStrategy implements RecommendationStrategy {

    @Override
    public List<Reproduzivel> recomendar(Playlist playlistBase, List<Midia> catalogoGlobal, int limite)
            throws PlaylistVaziaException, CatalogoInsuficienteException {

        if (playlistBase == null || playlistBase.getItens().isEmpty()) {
            throw new PlaylistVaziaException("A playlist base está vazia. Não é possível calcular afinidade.");
        }

        Map<String, Integer> frequenciaTags = new HashMap<>();
        for (Reproduzivel item : playlistBase.getItens()) {
            for (String tag : item.getTags()) {
                String tagNormalizada = tag.toLowerCase().trim();
                frequenciaTags.put(tagNormalizada, frequenciaTags.getOrDefault(tagNormalizada, 0) + 1);
            }
        }

        List<Reproduzivel> candidatos = new ArrayList<>();
        Map<Reproduzivel, Integer> pontuacao = new HashMap<>();

        for (Midia midia : catalogoGlobal) {
            if (playlistBase.getItens().contains(midia)) {
                continue;
            }

            int score = 0;
            for (String tag : midia.getTags()) {
                score += frequenciaTags.getOrDefault(tag.toLowerCase().trim(), 0);
            }

            if (score > 0) {
                candidatos.add(midia);
                pontuacao.put(midia, score);
            }
        }

        if (candidatos.isEmpty()) {
            throw new CatalogoInsuficienteException("Nenhuma mídia compatível foi encontrada no catálogo.");
        }

        candidatos.sort((m1, m2) -> Integer.compare(pontuacao.get(m2), pontuacao.get(m1)));

        return candidatos.subList(0, Math.min(limite, candidatos.size()));
    }
}