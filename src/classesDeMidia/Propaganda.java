package classesDeMidia;

public class Propaganda extends Midia implements Reproduzivel {
    public Propaganda(String titulo, int duracaoSegundos) {
        super(titulo, duracaoSegundos, 2024);
    }

    @Override
    public void play() { System.out.println("Tocando propaganda: " + getTitulo()); }
    @Override
    public void pause() { System.out.println("Propaganda pausada."); }
    @Override
    public void stop() {
        System.out.println("Propaganda interrompida.");
    }

    @Override
    public java.util.List<String> getTags() {
        return new java.util.ArrayList<>();
    }


}