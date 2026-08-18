package organizacao;

import classesDeMidia.Faixa;
import java.util.ArrayList;
import java.util.List;

public class Album {
    private String nome;
    private String genero;
    private List<Faixa> faixas;

    public Album(String nome, String genero) {
        this.nome = nome;
        this.genero = genero;
        this.faixas = new ArrayList<>();
    }

    public String getGenero() { return genero; }
    public String getNome() { return nome; }
    public List<Faixa> getFaixas() { return faixas; }

    /*
     Adiciona uma faixa à lista do álbum e configura a referência bidirecional para que a faixa tenha acesso ao gênero do álbum.
     */
    public void adicionarFaixa(Faixa faixa) {
        faixa.setAlbum(this);
        faixas.add(faixa);
    }

    public void removerFaixa(Faixa faixa) {
        faixa.setAlbum(null);
        faixas.remove(faixa);
    }

    public void listarFaixas() {
        System.out.println("--- Faixas do Álbum: " + nome + " ---");
        for (Faixa f : faixas) {
            System.out.println("- " + f.getTitulo() + " por " + f.getArtista());
        }
    }

    public Faixa consultarFaixa(String titulo) {
        for (Faixa f : faixas) {
            if (f.getTitulo().equalsIgnoreCase(titulo)) return f;
        }
        return null;
    }
}