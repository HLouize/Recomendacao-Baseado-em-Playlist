package classesDeMidia;

import organizacao.Podcast;
import java.util.ArrayList;
import java.util.List;

/* Representa um episódio de podcast. Pode possuir tags temáticas específicas e recupera o assunto do podcast para auxiliar
no Motor de Recomendação.
 */
public class Episodio extends Midia implements Reproduzivel {
    private String descricao;
    private Podcast podcast;
    private List<String> tags;

    public Episodio(String titulo, double duracao, int anoDeLancamento, String descricao) {
        super(titulo, duracao, anoDeLancamento);
        this.descricao = descricao;
        this.tags = new ArrayList<>();
    }

    public String getDescricao() { return descricao; }

    public void setPodcast(Podcast podcast) { this.podcast = podcast; }
    public Podcast getPodcast() { return podcast; }

    public void adicionarTag(String tag) { this.tags.add(tag); }

    /*
     Recupera os metadados do episódio.Retorna as tags temáticas próprias acrescidas do assunto geral do Podcast.
     @return Lista de strings contendo tags e/ou o assunto do podcast.
     */
    public List<String> getTags() {
        List<String> metadados = new ArrayList<>(this.tags);
        if (podcast != null && podcast.getAssunto() != null) {
            if (!metadados.contains(podcast.getAssunto())) {
                metadados.add(podcast.getAssunto());
            }
        }
        return metadados;
    }

    @Override
    public void play() { System.out.println("Reproduzindo episódio: " + getTitulo()); }
    @Override
    public void pause() { System.out.println("Episódio pausado: " + getTitulo()); }
    @Override
    public void stop() { System.out.println("Episódio parado: " + getTitulo()); }
}