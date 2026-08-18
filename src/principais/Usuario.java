package principais;

import classesDeMidia.Midia;
import planos.Plano;

public class Usuario {
	private String nome;
    private String email;
    private Plano plano;

    public Usuario(String nome, String email, Plano plano) {
        this.nome = nome;
        this.email = email;
        this.plano = plano;
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    
    public Plano consultarPlano() { return this.plano; }
    
    public void trocarPlano(Plano novoPlano) {
        this.plano = novoPlano;
        System.out.println("Usuário " + nome + " alterou o plano para: " + novoPlano.getDescricao());
    }

    public void acessarMidia(Midia midia) {
        System.out.println("\n[Acesso] " + nome + " está acessando a mídia: " + midia.getTitulo());
        if (plano.exibirPropaganda()) {
            System.out.println(">> [PROPAGANDA] Assine o Premium para remover os anúncios!");
        }
    }

    @Override
    public String toString() {
        return "Usuario: " + nome + " | Email: " + email + " | Plano Atual: " + plano.getDescricao();
    }
}
