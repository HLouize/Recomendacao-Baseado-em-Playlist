package planos;
public abstract class Plano {
    private String descricao;
    private double valor;

    public Plano(String descricao, double valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public boolean exibirPropaganda() { return true; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
}
