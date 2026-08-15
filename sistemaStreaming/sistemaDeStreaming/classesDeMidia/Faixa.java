package classesDeMidia;

public class Faixa extends Midia implements Reproduzivel{
	private String artista;
	
	public Faixa(String titulo, double duracao, int anoDeLancamento, String artista) {
        super(titulo, duracao, anoDeLancamento);
        this.artista = artista;
    }
	
	public String getArtista() { return artista; }

    @Override
    public void play() { System.out.println("Tocando a faixa: " + getTitulo() + " - " + artista); }
    @Override
    public void pause() { System.out.println("Faixa pausada: " + getTitulo()); }
    @Override
    public void stop() { System.out.println("Faixa parada: " + getTitulo()); }
}


