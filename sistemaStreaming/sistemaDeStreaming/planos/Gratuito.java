package planos;

public class Gratuito extends Plano {
    public Gratuito() {
        super("Plano Gratuito com Anúncios", 0.0);
    }

    @Override
    public boolean exibirPropaganda() { return true; }

    @Override
    public void setValor(double valor) { super.setValor(0.0); }
}