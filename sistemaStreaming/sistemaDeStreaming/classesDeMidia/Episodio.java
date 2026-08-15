package classesDeMidia;

public class Episodio extends Midia implements Reproduzivel {
    private String descricao;

    public Episodio(String titulo, double duracao, int anoDeLancamento, String descricao) {
        super(titulo, duracao, anoDeLancamento);
        this.descricao = descricao;
    }

    public String getDescricao() { return descricao; }

    @Override
    public void play() { System.out.println("Reproduzindo episódio: " + getTitulo()); }
    @Override
    public void pause() { System.out.println("Episódio pausado: " + getTitulo()); }
    @Override
    public void stop() { System.out.println("Episódio parado: " + getTitulo()); }
}