package principais;

import classesDeMidia.Audiobook;
import classesDeMidia.Episodio;
import classesDeMidia.Faixa;

// Organização
import organizacao.Album;
import organizacao.Playlist;
import organizacao.Podcast;

// Planos
import planos.Gratuito;
import planos.PlanoEstudante;
import planos.PlanoFamily;
import planos.PlanoIndividual;

public class Main {
 public static void main(String[] args) {
	 
     System.out.println("=== INICIALIZANDO TESTES DO STREAMING DE ÁUDIO ===\n");

   
     Usuario user1 = new Usuario("Héllida", "hellida@gmail.com", new Gratuito());
     Usuario user2 = new Usuario("Livia", "livia@gmail.com", new Gratuito());
     Usuario user3 = new Usuario("Guilherme", "guilherme@gmail.com", new Gratuito());

     System.out.println(user1);
     System.out.println(user2);
     System.out.println(user3);
     System.out.println();

     
     user1.trocarPlano(new PlanoIndividual(23.90));
     user2.trocarPlano(new PlanoIndividual(23.90));
     user3.trocarPlano(new PlanoEstudante(11.90));
     System.out.println();

    
     PlanoFamily planoFamilia = new PlanoFamily(44.90);
     user1.trocarPlano(planoFamilia);
     
     planoFamilia.adicionarUsuario(user1);
     planoFamilia.adicionarUsuario(user2);
     planoFamilia.adicionarUsuario(user3);
    
     planoFamilia.adicionarUsuario(new Usuario("Dep4", "d4@e.com", planoFamilia));
     planoFamilia.adicionarUsuario(new Usuario("Dep5", "d5@e.com", planoFamilia));
     planoFamilia.adicionarUsuario(new Usuario("Dep6", "d6@e.com", planoFamilia));
     planoFamilia.adicionarUsuario(new Usuario("Dep7", "d7@e.com", planoFamilia)); // Deve dar erro

     planoFamilia.listarUsuarios();
     System.out.println();

    
     Album album = new Album("Powerslave", "Heavy Metal");
     album.adicionarFaixa(new Faixa("Aces High", 4.31, 1984, "Iron Maiden"));
     album.adicionarFaixa(new Faixa("Two Minutes to Midnight", 6.03, 1984, "Iron Maiden"));
     album.adicionarFaixa(new Faixa("Powerslave", 7.11, 1984, "Iron Maiden"));
     album.adicionarFaixa(new Faixa("Rime of the Ancient Mariner", 13.38, 1984, "Iron Maiden"));
     album.adicionarFaixa(new Faixa("Flash of the Blade", 4.05, 1984, "Iron Maiden"));

   
     Podcast podcast = new Podcast("Tia Lu Podcasts", "Tecnologia", "Conversas sobre programação");
     podcast.adicionarEpisodio(new Episodio("Ep 01: O que é OOP?", 45.0, 2026, "Introdução a POO"));
     podcast.adicionarEpisodio(new Episodio("Ep 02: Interfaces vs Abstratas", 50.0, 2026, "Diferenças fundamentais"));
     podcast.adicionarEpisodio(new Episodio("Ep 03: Arquitetura de Software", 60.0, 2026, "Padrões limpos"));

  
     Audiobook audiobook = new Audiobook("1984", 690.0, 1949, "George Orwell");

     
     Playlist playlist = new Playlist("Minhas Favoritas");
     playlist.adicionarItem(album.getFaixas().get(3)); 
     playlist.adicionarItem(podcast.getEpisodios().get(1));
     playlist.adicionarItem(audiobook); 

     
     Player player = new Player();

     
     System.out.println("\n=== Testando validação de acessos e propagandas ===");
     Usuario usuarioGratuito = new Usuario("Luciana", "lulu@email.com", new Gratuito());
     
     
     usuarioGratuito.acessarMidia(audiobook);
     player.tocarItem(audiobook);

     
     user2.acessarMidia(album.getFaixas().get(3));
     player.tocarItem(album.getFaixas().get(3));

     
     player.executarPlaylist(playlist);
     player.executarAlbum(album);
     player.executarPodcast(podcast);
 }
}