package planos;

public class PlanoIndividual extends Plano {
    public PlanoIndividual(double valor) {
        super("Plano Pago Padrão Individual", valor);
    }

    @Override
    public boolean exibirPropaganda() { return false; }
}