package principais;

import classesDeMidia.Faixa;
import classesDeMidia.Midia;
import organizacao.Album;
import organizacao.Playlist;
import planos.Gratuito;
import planos.Plano;
import planos.PlanoEstudante;
import planos.PlanoFamily;
import planos.PlanoIndividual;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Midia> catalogoGlobal = carregarCatalogoDiversificado();
        Player player = new Player();

        System.out.println("=========================================");
        System.out.println("    SISTEMA DE MUSICA INTERATIVO         ");
        System.out.println("=========================================");

        System.out.print("\nDigite seu nome de usuario: ");
        String nome = scanner.nextLine();

        System.out.println("\nEscolha o seu Plano:");
        System.out.println("1. Gratuito (com anuncios)");
        System.out.println("2. Individual");
        System.out.println("3. Estudante");
        System.out.println("4. Familia");
        System.out.print("Opcao: ");
        int opcaoPlano = Integer.parseInt(scanner.nextLine());

        Plano plano = switch (opcaoPlano) {
            case 2 -> new PlanoIndividual();
            case 3 -> new PlanoEstudante();
            case 4 -> new PlanoFamily();
            default -> new Gratuito();
        };

        Usuario usuario = new Usuario(nome, plano);
        System.out.println("Usuario " + usuario.getNome() + " cadastrado com sucesso!");

        System.out.print("\nDigite o nome para a sua Playlist: ");
        String nomePlaylist = scanner.nextLine();
        Playlist playlistUsuario = new Playlist(nomePlaylist);

        boolean rodando = true;
        while (rodando) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Ver catalogo de musicas");
            System.out.println("2. Adicionar musica do catalogo a minha playlist");
            System.out.println("3. Ver minha playlist");
            System.out.println("4. Dar Play na playlist");
            System.out.println("5. Parar musica");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> {
                    System.out.println("\n--- CATALOGO DISPONIVEL ---");
                    for (int i = 0; i < catalogoGlobal.size(); i++) {
                        Midia m = catalogoGlobal.get(i);
                        if (m instanceof Faixa f) {
                            System.out.printf("%d. %s - %s [%s]\n", (i + 1), f.getTitulo(), f.getArtista(), f.getTags());
                        }
                    }
                }
                case 2 -> {
                    System.out.println("\n--- ADICIONAR A PLAYLIST ---");
                    for (int i = 0; i < catalogoGlobal.size(); i++) {
                        System.out.printf("%d. %s - %s\n", (i + 1), catalogoGlobal.get(i).getTitulo(), ((Faixa) catalogoGlobal.get(i)).getArtista());
                    }
                    System.out.print("Digite o numero da musica: ");
                    int num = Integer.parseInt(scanner.nextLine());
                    if (num > 0 && num <= catalogoGlobal.size()) {
                        playlistUsuario.adicionarItem(catalogoGlobal.get(num - 1));
                        System.out.println("'" + catalogoGlobal.get(num - 1).getTitulo() + "' adicionada com sucesso!");
                    } else {
                        System.out.println("[ERRO] Opcao invalida!");
                    }
                }
                case 3 -> {
                    System.out.println("\n--- MINHA PLAYLIST: " + playlistUsuario.getNome() + " ---");
                    if (playlistUsuario.getItens().isEmpty()) {
                        System.out.println("(Sua playlist esta vazia)");
                    } else {
                        playlistUsuario.getItens().forEach(item -> System.out.println("- " + item.getTitulo()));
                    }
                }
                case 4 -> player.iniciarReproducao(usuario, playlistUsuario, catalogoGlobal);
                case 5 -> player.parar();
                case 0 -> {
                    rodando = false;
                    System.out.println("Encerrando a aplicacao...");
                }
                default -> System.out.println("[ERRO] Opcao invalida!");
            }
        }

        scanner.close();
    }

    private static List<Midia> carregarCatalogoDiversificado() {
        List<Midia> catalogo = new ArrayList<>();

        Album albumSoad = new Album("Toxicity", "System of a Down");
        Album albumScorpions = new Album("Crazy World", "Scorpions");
        Album albumQueen = new Album("A Night at the Opera", "Queen");
        Album album1D = new Album("Midnight Memories", "One Direction");

        Album albumCaetano = new Album("Transa", "Caetano Veloso");
        Album albumZeca = new Album("Deixa a Vida Me Levar", "Zeca Pagodinho");
        Album albumTchaikovsky = new Album("1812 Overture", "Pyotr Ilyich Tchaikovsky");
        Album albumShostakovich = new Album("Symphony No. 5", "Dmitri Shostakovich");

        Faixa r1 = new Faixa("Chop Suey!", 210, "System of a Down", albumSoad);
        r1.adicionarTag("Rock");
        r1.adicionarTag("Metal");

        Faixa r2 = new Faixa("Toxicity", 219, "System of a Down", albumSoad);
        r2.adicionarTag("Rock");
        r2.adicionarTag("Metal");

        Faixa r3 = new Faixa("Wind of Change", 312, "Scorpions", albumScorpions);
        r3.adicionarTag("Rock");
        r3.adicionarTag("Classic Rock");

        Faixa r4 = new Faixa("Rock You Like a Hurricane", 255, "Scorpions", albumScorpions);
        r4.adicionarTag("Rock");
        r4.adicionarTag("Hard Rock");

        Faixa r5 = new Faixa("Bohemian Rhapsody", 354, "Queen", albumQueen);
        r5.adicionarTag("Rock");

        Faixa r6 = new Faixa("Don't Stop Me Now", 209, "Queen", albumQueen);
        r6.adicionarTag("Rock");

        Faixa p1 = new Faixa("Story of My Life", 245, "One Direction", album1D);
        p1.adicionarTag("Pop");
        p1.adicionarTag("Pop Rock");

        Faixa p2 = new Faixa("What Makes You Beautiful", 200, "One Direction", album1D);
        p2.adicionarTag("Pop");

        Faixa m1 = new Faixa("You Don't Know Me", 230, "Caetano Veloso", albumCaetano);
        m1.adicionarTag("MPB");

        Faixa m2 = new Faixa("Sampa", 185, "Caetano Veloso", albumCaetano);
        m2.adicionarTag("MPB");

        Faixa s1 = new Faixa("Deixa a Vida Me Levar", 270, "Zeca Pagodinho", albumZeca);
        s1.adicionarTag("Samba");

        Faixa s2 = new Faixa("OGUM", 290, "Zeca Pagodinho", albumZeca);
        s2.adicionarTag("Samba");

        Faixa c1 = new Faixa("1812 Overture, Op. 49", 900, "Pyotr Ilyich Tchaikovsky", albumTchaikovsky);
        c1.adicionarTag("Classica");

        Faixa c2 = new Faixa("Waltz No. 2", 220, "Dmitri Shostakovich", albumShostakovich);
        c2.adicionarTag("Classica");

        catalogo.add(r1);
        catalogo.add(r2);
        catalogo.add(r3);
        catalogo.add(r4);
        catalogo.add(r5);
        catalogo.add(r6);
        catalogo.add(p1);
        catalogo.add(p2);
        catalogo.add(m1);
        catalogo.add(m2);
        catalogo.add(s1);
        catalogo.add(s2);
        catalogo.add(c1);
        catalogo.add(c2);

        return catalogo;
    }
}
