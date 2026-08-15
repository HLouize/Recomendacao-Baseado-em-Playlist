package organizacao;
import classesDeMidia.Midia;
import classesDeMidia.Reproduzivel;
import java.util.ArrayList;
import java.util.List;
 


public class Playlist {
	private String nome;
    private List<Reproduzivel> itens;

    public Playlist(String nome) {
        this.nome = nome;
        this.itens = new ArrayList<>();
    }

    public String getNome() { return nome; }
    public List<Reproduzivel> getItens() { return itens; }

    public void adicionarItem(Reproduzivel item) { itens.add(item); }
    public void removerItem(Reproduzivel item) { itens.remove(item); }

    public void listarItens() {
        System.out.println("--- Itens da Playlist: " + nome + " ---");
        for (Reproduzivel item : itens) {
            if (item instanceof Midia) {
                System.out.println("- " + ((Midia) item).getTitulo() + " [" + item.getClass().getSimpleName() + "]");
            }
        }
    }

    public Reproduzivel consultarItem(String titulo) {
        for (Reproduzivel item : itens) {
            if (item instanceof Midia && ((Midia) item).getTitulo().equalsIgnoreCase(titulo)) {
                return item;
            }
        }
        return null;
    }

    public int quantidadeItens() { return itens.size(); }
}

