package principais;

import java.util.List;
import java.util.ArrayList;
import classesDeMidia.*;
import organizacao.Playlist;

public class Player {
    private boolean emReproducao = true;

    public void iniciarReproducao(Usuario usuario, Playlist playlist, List<Midia> catalogoGlobal) {
        System.out.println("\n--- REPRODUZINDO PLAYLIST: " + playlist.getNome() + " ---");

        List<Reproduzivel> fila = new ArrayList<>(playlist.getItens());
        int index = 0;

        // Limite de músicas automáticas (Autoplay vai recomendar umas 3 vezes e parar, como você pediu)
        int limiteAutoplay = 3;
        int musicasRecomendadasTocadas = 0;

        while (index < fila.size() && emReproducao) {
            Reproduzivel item = fila.get(index);

            System.out.println("\n[ ♪ TOCANDO AGORA ]");
            item.play();

            if (item instanceof Midia) {
                usuario.acessarMidia((Midia) item);
            }
            System.out.println("----------------------------------------");
            index++;

            // QUANDO CHEGAR NA ÚLTIMA MÚSICA DA FILA, ELE BUSCA A PRÓXIMA PELA TAG
            if (index == fila.size() && emReproducao && musicasRecomendadasTocadas < limiteAutoplay) {

                // Pega estritamente a última mídia que acabou de tocar
                Reproduzivel ultimaTocada = fila.get(index - 1);

                List<String> tagsDaUltima = new ArrayList<>();
                if (ultimaTocada instanceof Faixa f) {
                    tagsDaUltima = f.getTags();
                }

                if (!tagsDaUltima.isEmpty()) {
                    System.out.println("\n[AUTOPLAY] Buscando próxima recomendação baseada na tag: " + tagsDaUltima + "...");
                    boolean achouRecomendacao = false;

                    // Procura no catálogo UMA música com a mesma tag que ainda não tocou
                    for (Midia m : catalogoGlobal) {
                        if (m instanceof Faixa f && !fila.contains(m)) {
                            boolean temMesmaTag = false;
                            for (String tag : f.getTags()) {
                                if (tagsDaUltima.contains(tag)) {
                                    temMesmaTag = true;
                                    break;
                                }
                            }

                            if (temMesmaTag) {
                                // Adiciona na fila e deixa o While tocar ela na próxima rodada!
                                fila.add(f);
                                musicasRecomendadasTocadas++;
                                achouRecomendacao = true;
                                System.out.println("-> Música recomendada encontrada: " + f.getTitulo());
                                break; // Achou uma música, para de procurar no catálogo
                            }
                        }
                    }

                    if (!achouRecomendacao) {
                        System.out.println("-> Nenhuma música nova com essa mesma tag encontrada. Fim da rádio.");
                        break;
                    }
                } else {
                    System.out.println("-> A mídia atual não possui tags para basear a recomendação.");
                    break;
                }

            } else if (index == fila.size() && musicasRecomendadasTocadas >= limiteAutoplay) {
                System.out.println("\n-> Limite de recomendações atingido (3 músicas). Fim da reprodução automática.");
            }
        }
    }

    public void parar() {
        this.emReproducao = false;
        System.out.println("\n[Player] Reprodução interrompida.");
    }
}