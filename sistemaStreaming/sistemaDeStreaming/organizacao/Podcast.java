package organizacao;
import classesDeMidia.Episodio;
import java.util.ArrayList;
import java.util.List;

public class Podcast{
private String nome;
private String assunto;
private String descricao;
private List<Episodio> episodios;

public Podcast(String nome, String assunto, String descricao) {
    this.nome = nome;
    this.assunto = assunto;
    this.descricao = descricao;
    this.episodios = new ArrayList<>();
}

public String getNome() { return nome; }
public List<Episodio> getEpisodios() { return episodios; }
public String getAssunto() { 
    return assunto; 
}

public String getDescricao() { 
    return descricao; 
}

public void adicionarEpisodio(Episodio ep) { episodios.add(ep); }
public void removerEpisodio(Episodio ep) { episodios.remove(ep); }

public void listarEpisodios() {
    System.out.println("--- Episódios do Podcast: " + nome + " ---");
    for (Episodio ep : episodios) {
        System.out.println("- " + ep.getTitulo() + " (" + ep.getDuracao() + " min)");
    }
}

public Episodio consultarEpisodio(String titulo) {
    for (Episodio ep : episodios) {
        if (ep.getTitulo().equalsIgnoreCase(titulo)) return ep;
    }
    return null;
}
}