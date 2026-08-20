package principais;

import classesDeMidia.Midia;
import classesDeMidia.Reproduzivel;
import planos.Plano;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private String email;
    private Plano plano;


    private List<Reproduzivel> historico;


    public Usuario(String nome, String email, Plano plano) {
        this.nome = nome;
        this.email = email;
        this.plano = plano;
        this.historico = new ArrayList<>();
    }

    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.historico = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }

    public Plano consultarPlano() { return this.plano; }

    public void trocarPlano(Plano novoPlano) {
        this.plano = novoPlano;
        System.out.println("Usuário " + nome + " alterou o plano para: " + novoPlano.getDescricao());
    }

    public Plano getPlano() { return this.plano; }

    public void setPlano(Plano plano) { this.plano = plano; }

    public List<Reproduzivel> getHistorico() { return this.historico; }

    public void acessarMidia(Midia midia) {
        System.out.println("\n[Acesso] " + nome + " está acessando a mídia: " + midia.getTitulo());

        if (midia instanceof Reproduzivel) {
            historico.add((Reproduzivel) midia);
        }

        if (plano != null && plano.exibirPropaganda()) {
            System.out.println(">> [PROPAGANDA] Assine o Premium para remover os anúncios!");
        }
    }

    @Override
    public String toString() {
        String descPlano = (plano != null) ? plano.getDescricao() : "Sem plano vinculado";
        return "Usuario: " + nome + " | Email: " + email + " | Plano Atual: " + descPlano;
    }
}