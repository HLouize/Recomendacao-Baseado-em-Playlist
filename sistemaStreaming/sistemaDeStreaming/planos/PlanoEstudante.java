package planos;

public class PlanoEstudante extends Plano {
    public PlanoEstudante(double valor) {
        super("Plano Universitário com Desconto", valor);
    }

    @Override
    public boolean exibirPropaganda() { return false; }
}