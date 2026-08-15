package classesDeMidia;

/* Representa um Audiobook.Armazena um atributo específico de categoria ou gênero para cálculo de
similaridade nas heurísticas de recomendação.
*/
public class Audiobook extends Midia implements Reproduzivel {
    private String autor;
    private String categoria;

    public Audiobook(String titulo, double duracao, int anoDeLancamento, String autor, String categoria) {
        super(titulo, duracao, anoDeLancamento);
        this.autor = autor;
        this.categoria = categoria;
    }

    public String getAutor() { return autor; }

    // Recupera a categoria ou gênero do audiobook. @return A categoria em formato String (ex: ficção, educacional).

    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public void play() { System.out.println("Narrando audiobook: " + getTitulo() + " (Autor: " + autor + ")"); }
    @Override
    public void pause() { System.out.println("Audiobook pausado: " + getTitulo()); }
    @Override
    public void stop() { System.out.println("Audiobook parado: " + getTitulo()); }
}