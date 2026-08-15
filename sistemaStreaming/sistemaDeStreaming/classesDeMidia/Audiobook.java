package classesDeMidia;

public class Audiobook extends Midia implements Reproduzivel {
    private String autor;

    public Audiobook(String titulo, double duracao, int anoDeLancamento, String autor) {
        super(titulo, duracao, anoDeLancamento);
        this.autor = autor;
    }

    public String getAutor() { return autor; }

    @Override
    public void play() { System.out.println("Narrando audiobook: " + getTitulo() + " (Autor: " + autor + ")"); }
    @Override
    public void pause() { System.out.println("Audiobook pausado: " + getTitulo()); }
    @Override
    public void stop() { System.out.println("Audiobook parado: " + getTitulo()); }
}