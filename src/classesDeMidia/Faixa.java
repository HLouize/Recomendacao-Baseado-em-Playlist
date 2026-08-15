package classesDeMidia;

import organizacao.Album;
import java.util.ArrayList;
import java.util.List;

/*
 * Representa uma faixa musical. Possui tags próprias e é capaz de recuperar o gênero do seu álbum associado
 * para o cálculo de similaridade no Motor de Recomendação.
 */
public class Faixa extends Midia implements Reproduzivel {
    private String artista;
    private Album album;
    private List<String> tags;

    public Faixa(String titulo, double duracao, int anoDeLancamento, String artista) {
        super(titulo, duracao, anoDeLancamento);
        this.artista = artista;
        this.tags = new ArrayList<>();
    }

    public String getArtista() { return artista; }

    public void setAlbum(Album album) { this.album = album; }
    public Album getAlbum() { return album; }

    public void adicionarTag(String tag) { this.tags.add(tag); }

    /*
     * Recupera os metadados da faixa. Retorna as tags próprias acrescidas do gênero do álbum associado.
     * @return Lista de strings contendo tags e/ou o gênero musical.
     */
    public List<String> getTags() {
        List<String> metadados = new ArrayList<>(this.tags);
        if (album != null && album.getGenero() != null) {
            if (!metadados.contains(album.getGenero())) {
                metadados.add(album.getGenero());
            }
        }
        return metadados;
    }

    @Override
    public void play() { System.out.println("Tocando a faixa: " + getTitulo() + " - " + artista); }
    @Override
    public void pause() { System.out.println("Faixa pausada: " + getTitulo()); }
    @Override
    public void stop() { System.out.println("Faixa parada: " + getTitulo()); }
}