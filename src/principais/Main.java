package principais;

import classesDeMidia.Faixa;
import classesDeMidia.Midia;
import classesDeMidia.Reproduzivel;
import excecoes.CatalogoInsuficienteException;
import excecoes.PlaylistVaziaException;
import filtros.AntiRepetitionFilter;
import organizacao.Album;
import organizacao.Playlist;
import planos.Gratuito;
import planos.Plano;
import planos.PlanoEstudante;
import planos.PlanoFamily;
import planos.PlanoIndividual;
import recomendacao.PlaylistSimilarityStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Midia> catalogoGlobal = carregarCatalogoDiversificado();
        Player player = new Player();

        
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

        Plano planoEscolhido = switch (opcaoPlano) {
            case 2 -> new PlanoIndividual(19.90);
            case 3 -> new PlanoEstudante(9.90);
            case 4 -> new PlanoFamily(34.90);
            default -> new Gratuito(0.0);
        };

        Usuario usuario = new Usuario(nome, planoEscolhido);
        System.out.println("\nConta criada com sucesso para: " + usuario.getNome());

        System.out.print("Digite o nome para a sua Playlist: ");
        String nomePlaylist = scanner.nextLine();
        Playlist playlistUsuario = new Playlist(nomePlaylist);

        boolean rodando = true;
        while (rodando) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Ver catalogo de musicas");
            System.out.println("2. Adicionar musica a minha playlist");
            System.out.println("3. Ver minha playlist");
            System.out.println("4. Dar Play na playlist (e iniciar recomendacao)");
            System.out.println("5. Parar musica");
            System.out.println("6. GERAR NOVA PLAYLIST RECOMENDADA");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opcao: ");

            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> {
                    System.out.println("\n--- CATALOGO DISPONIVEL ---");
                    for (int i = 0; i < catalogoGlobal.size(); i++) {
                        Midia m = catalogoGlobal.get(i);
                        if (m instanceof Faixa f) {
                            System.out.printf("%d. %s - %s [Tag: %s]\n", (i + 1), f.getNome(), f.getArtista(), f.getTags());
                        }
                    }
                }
                case 2 -> {
                    System.out.println("\n--- ADICIONAR A PLAYLIST ---");
                    for (int i = 0; i < catalogoGlobal.size(); i++) {
                        Midia m = catalogoGlobal.get(i);
                        if (m instanceof Faixa f) {
                            System.out.printf("%d. %s - %s\n", (i + 1), f.getNome(), f.getArtista());
                        } else {
                            System.out.printf("%d. %s\n", (i + 1), m.getNome());
                        }
                    }
                    System.out.print("Digite o numero da musica: ");
                    int num = Integer.parseInt(scanner.nextLine());
                    if (num > 0 && num <= catalogoGlobal.size()) {
                        playlistUsuario.adicionarItem(catalogoGlobal.get(num - 1));
                        Midia m = catalogoGlobal.get(num - 1);
                        System.out.println("Musica '" + m.getNome() + "' adicionada com sucesso!");
                    } else {
                        System.out.println("[ERRO] Opcao invalida!");
                    }
                }
                case 3 -> {
                    System.out.println("\n--- MINHA PLAYLIST: " + playlistUsuario.getNome() + " ---");
                    if (playlistUsuario.getItens().isEmpty()) {
                        System.out.println("(Sua playlist esta vazia)");
                    } else {
                        for (Reproduzivel item : playlistUsuario.getItens()) {
                            if (item instanceof Midia m) {
                                System.out.println("- " + m.getNome());
                            }
                        }
                    }
                }
                case 4 -> player.iniciarReproducao(usuario, playlistUsuario, catalogoGlobal);
                case 5 -> player.parar();
                case 6 -> {
                    if (playlistUsuario.getItens().isEmpty()) {
                        System.out.println("[AVISO] Sua playlist esta vazia! Adicione musicas primeiro para basearmos a recomendacao.");
                    } else {
                        System.out.println("\n--- GERANDO PLAYLIST RECOMENDADA ---");
                        try {
                            RecommendationEngine engine = new RecommendationEngine(new PlaylistSimilarityStrategy());
                            engine.adicionarFiltro(new AntiRepetitionFilter());

                            Playlist novaPlaylistRecomendada = engine.gerarPlaylistRecomendada(
                                    "Mix Recomendado para " + usuario.getNome(),
                                    playlistUsuario,
                                    usuario,
                                    catalogoGlobal,
                                    3 // Puxando 3 musicas recomendadas
                            );

                            System.out.println("Playlist '" + novaPlaylistRecomendada.getNome() + "' criada com sucesso!");
                            System.out.println("Musicas adicionadas baseadas no seu gosto:");
                            for (Reproduzivel item : novaPlaylistRecomendada.getItens()) {
                                if (item instanceof Midia m) {
                                    System.out.println("- " + m.getNome());
                                }
                            }
                        } catch (PlaylistVaziaException | CatalogoInsuficienteException e) {
                            System.out.println("[ERRO] Nao foi possivel gerar a playlist: " + e.getMessage());
                        }
                    }
                }
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

        Album albumRock = new Album("Toxicity", "System of a Down");
        Album albumScorpions = new Album("Crazy World", "Scorpions");
        Album albumPop = new Album("Midnight Memories", "One Direction");
        Album albumSamba = new Album("Deixa a Vida Me Levar", "Zeca Pagodinho");

        // Musicas de Rock
        Faixa r1 = new Faixa("Chop Suey!", 210, "System of a Down", albumRock);
        r1.adicionarTag("Rock");

        Faixa r2 = new Faixa("Wind of Change", 312, "Scorpions", albumScorpions);
        r2.adicionarTag("Rock");

        Faixa r3 = new Faixa("Rock You Like a Hurricane", 255, "Scorpions", albumScorpions);
        r3.adicionarTag("Rock");

        Faixa r4 = new Faixa("Toxicity", 219, "System of a Down", albumRock);
        r4.adicionarTag("Rock");

        // Musicas Pop
        Faixa p1 = new Faixa("Story of My Life", 245, "One Direction", albumPop);
        p1.adicionarTag("Pop");

        Faixa p2 = new Faixa("What Makes You Beautiful", 200, "One Direction", albumPop);
        p2.adicionarTag("Pop");

        // Musicas Samba
        Faixa s1 = new Faixa("Deixa a Vida Me Levar", 270, "Zeca Pagodinho", albumSamba);
        s1.adicionarTag("Samba");

        Faixa s2 = new Faixa("OGUM", 290, "Zeca Pagodinho", albumSamba);
        s2.adicionarTag("Samba");

        catalogo.add(r1);
        catalogo.add(r2);
        catalogo.add(r3);
        catalogo.add(r4);
        catalogo.add(p1);
        catalogo.add(p2);
        catalogo.add(s1);
        catalogo.add(s2);

        return catalogo;
    }
}
