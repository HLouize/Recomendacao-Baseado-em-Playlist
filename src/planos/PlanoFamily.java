package planos;

import excecoes.LimiteUsuariosPlanoException;
import principais.Usuario;
import java.util.ArrayList;
import java.util.List;

public class PlanoFamily extends Plano {
    private List<Usuario> dependentes;
    private static final int LIMITE = 6;

    public PlanoFamily(double valor) {
        super("Plano Familiar Multiusuário", valor);
        this.dependentes = new ArrayList<>();
    }

    public boolean adicionarUsuario(Usuario u) {
       // lança a exceção customizada se o limite for atingido
        if (dependentes.size() >= LIMITE) {
            throw new LimiteUsuariosPlanoException("Erro: Limite máximo de " + LIMITE + " usuários atingido no Plano Family.");
        }

        dependentes.add(u);
        return true;
    }

    public void removerUsuario(Usuario u) {
        dependentes.remove(u);
    }

    public void listarUsuarios() {
        System.out.println("--- Membros do Plano Family ---");
        for (Usuario u : dependentes) {
            System.out.println("- " + u.getNome() + " (" + u.getEmail() + ")");
        }
    }

    @Override
    public boolean exibirPropaganda() {
        return false;
    }
}