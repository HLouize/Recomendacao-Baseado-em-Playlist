package principais;

import classesDeMidia.*;
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

        // CRIANDO O PLANO FAMÍLIA COMPARTILHADO DA APLICAÇÃO
        PlanoFamily planoFamiliaCompartilhado = new PlanoFamily(34.90);
        List<String> membrosFamilia = new ArrayList<>();

        System.out.println("=========================================");
        System.out.println("    SISTEMA DE MUSICA INTERATIVO         ");
        System.out.println("=========================================");

        boolean sistemaLigado = true;

        while (sistemaLigado) {
            System.out.println("\n--- TELA DE LOGIN / CADASTRO ---");
            System.out.print("Digite seu nome de usuario (ou digite 'sair' para fechar tudo): ");
            String nome = scanner.nextLine();

            if (nome.equalsIgnoreCase("sair")) {
                sistemaLigado = false;
                System.out.println("Encerrando a aplicação... Até logo!");
                break;
            }

            System.out.println("\nEscolha o seu Plano:");
            System.out.println("1. Gratuito (com anuncios)");
            System.out.println("2. Individual");
            System.out.println("3. Estudante");
            System.out.println("4. Familia (Plano Compartilhado)");
            System.out.print("Opcao: ");
            int opcaoPlano = Integer.parseInt(scanner.nextLine());

            Plano planoEscolhido;

            // LÓGICA DE ATRIBUIÇÃO DE PLANO
            switch (opcaoPlano) {
                case 2 -> planoEscolhido = new PlanoIndividual(19.90);
                case 3 -> planoEscolhido = new PlanoEstudante(9.90);
                case 4 -> {
                    planoEscolhido = planoFamiliaCompartilhado;
                    if (!membrosFamilia.contains(nome)) {
                        membrosFamilia.add(nome);
                    }
                    System.out.println("\n[SUCESSO] Você entrou no Plano Família Compartilhado!");
                    System.out.println("-> Integrantes atuais da Família: " + membrosFamilia);
                }
                default -> planoEscolhido = new Gratuito();
            }

            String emailGerado = nome.toLowerCase().replace(" ", "") + "@email.com";
            Usuario usuario = new Usuario(nome, emailGerado, planoEscolhido);

            System.out.println("\n-> Conta acessada com sucesso por: " + usuario.getNome());

            List<Playlist> minhasPlaylists = new ArrayList<>();

            System.out.print("\nDigite o nome para a sua primeira Playlist: ");
            String nomePlaylist = scanner.nextLine();

            Playlist playlistUsuario = new Playlist(nomePlaylist);
            minhasPlaylists.add(playlistUsuario);

            boolean sessaoAtiva = true;

            while (sessaoAtiva) {
                System.out.println("\n=== MENU PRINCIPAL (" + usuario.getNome() + ") ===");
                System.out.println("1. Ver catalogo de midias (Musicas, Podcasts, Audiobooks)");
                System.out.println("2. Adicionar midia a minha playlist atual (" + playlistUsuario.getNome() + ")");
                System.out.println("3. Ver minha playlist atual (" + playlistUsuario.getNome() + ")");
                System.out.println("4. Dar Play na playlist atual (Inicia a Rádio Automática)");
                System.out.println("5. Parar player");
                System.out.println("6. GERAR NOVA PLAYLIST RECOMENDADA");
                System.out.println("7. Escolher e Tocar QUALQUER Playlist (Minhas ou Recomendadas)");
                System.out.println("8. Criar NOVA Playlist vazia");
                System.out.println("9. Mudar a Playlist atual (Para adicionar musicas nela)");
                System.out.println("0. FAZER LOGOUT (Trocar de Usuário)");
                System.out.println("10. Encerrar o Sistema");
                System.out.print("Escolha uma opcao: ");

                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1 -> {
                        System.out.println("\n--- CATALOGO DISPONIVEL ---");
                        for (int i = 0; i < catalogoGlobal.size(); i++) {
                            Midia m = catalogoGlobal.get(i);
                            String tipo = m.getClass().getSimpleName().toLowerCase();

                            if (m instanceof Faixa f) {
                                System.out.printf("%d. [🎵 MÚSICA] %s - %s\n", (i + 1), f.getTitulo(), f.getArtista());
                            } else if (tipo.contains("audiobook") || tipo.contains("capitulo")) {
                                System.out.printf("%d. [📚 AUDIOBOOK] %s\n", (i + 1), m.getTitulo());
                            } else if (tipo.contains("podcast") || tipo.contains("episodio")) {
                                System.out.printf("%d. [🎙️ PODCAST] %s\n", (i + 1), m.getTitulo());
                            } else {
                                System.out.printf("%d. [%s] %s\n", (i + 1), tipo.toUpperCase(), m.getTitulo());
                            }
                        }
                    }
                    case 2 -> {
                        System.out.println("\n--- ADICIONAR A PLAYLIST: " + playlistUsuario.getNome() + " ---");
                        for (int i = 0; i < catalogoGlobal.size(); i++) {
                            Midia m = catalogoGlobal.get(i);
                            String tipo = m.getClass().getSimpleName().toLowerCase();

                            if (m instanceof Faixa f) {
                                System.out.printf("%d. [ MÚSICA] %s - %s\n", (i + 1), f.getTitulo(), f.getArtista());
                            } else if (tipo.contains("audiobook") || tipo.contains("capitulo")) {
                                System.out.printf("%d. [ AUDIOBOOK] %s\n", (i + 1), m.getTitulo());
                            } else if (tipo.contains("podcast") || tipo.contains("episodio")) {
                                System.out.printf("%d. [️ PODCAST] %s\n", (i + 1), m.getTitulo());
                            } else {
                                System.out.printf("%d. [%s] %s\n", (i + 1), tipo.toUpperCase(), m.getTitulo());
                            }
                        }
                        System.out.print("Digite o numero da midia: ");
                        int num = Integer.parseInt(scanner.nextLine());
                        if (num > 0 && num <= catalogoGlobal.size()) {
                            playlistUsuario.adicionarItem((Reproduzivel) catalogoGlobal.get(num - 1));
                            System.out.println("-> Midia adicionada com sucesso na playlist '" + playlistUsuario.getNome() + "'!");
                        } else {
                            System.out.println("[ERRO] Opcao invalida!");
                        }
                    }
                    case 3 -> {
                        System.out.println("\n--- PLAYLIST ATUAL: " + playlistUsuario.getNome() + " ---");
                        if (playlistUsuario.getItens().isEmpty()) {
                            System.out.println("(Sua playlist esta vazia)");
                        } else {
                            for (Reproduzivel item : playlistUsuario.getItens()) {
                                if (item instanceof Midia m) {
                                    System.out.println("- " + m.getTitulo());
                                }
                            }
                        }
                    }
                    case 4 -> {
                        if (playlistUsuario.getItens().isEmpty()) {
                            System.out.println("[AVISO] A playlist '" + playlistUsuario.getNome() + "' esta vazia!");
                        } else {
                            player.iniciarReproducao(usuario, playlistUsuario, catalogoGlobal);
                        }
                    }
                    case 5 -> player.parar();
                    case 6 -> {
                        if (playlistUsuario.getItens().isEmpty()) {
                            System.out.println("[AVISO] Sua playlist atual esta vazia! Adicione musicas primeiro para basearmos a recomendacao.");
                        } else {
                            System.out.println("\n--- GERANDO PLAYLIST RECOMENDADA ---");
                            Playlist nova = new Playlist("Mix Recomendado baseado em " + playlistUsuario.getNome());
                            try {
                                RecommendationEngine engine = new RecommendationEngine(new PlaylistSimilarityStrategy());
                                engine.adicionarFiltro(new AntiRepetitionFilter());
                                nova = engine.gerarPlaylistRecomendada(nova.getNome(), playlistUsuario, usuario, catalogoGlobal, 3);
                            } catch (Exception e) {}

                            if (nova == null || nova.getItens().isEmpty()) {
                                int adicionadas = 0;
                                for (Midia m : catalogoGlobal) {
                                    if (adicionadas < 3 && !playlistUsuario.getItens().contains(m)) {
                                        nova.adicionarItem((Reproduzivel) m);
                                        adicionadas++;
                                    }
                                }
                            }

                            minhasPlaylists.add(nova);
                            System.out.println("-> Playlist '" + nova.getNome() + "' criada com " + nova.getItens().size() + " itens!");
                            for (Reproduzivel item : nova.getItens()) {
                                if (item instanceof Midia m) System.out.println("- " + m.getTitulo());
                            }
                        }
                    }
                    case 7 -> {
                        System.out.println("\n--- ESCOLHA A PLAYLIST PARA OUVIR ---");
                        for (int i = 0; i < minhasPlaylists.size(); i++) {
                            Playlist p = minhasPlaylists.get(i);
                            System.out.printf("%d. %s (%d itens)\n", (i + 1), p.getNome(), p.getItens().size());
                        }
                        System.out.print("Digite o numero da playlist que deseja tocar: ");
                        int escolhaPlaylist = Integer.parseInt(scanner.nextLine());

                        if (escolhaPlaylist > 0 && escolhaPlaylist <= minhasPlaylists.size()) {
                            Playlist selecionada = minhasPlaylists.get(escolhaPlaylist - 1);
                            if (selecionada.getItens().isEmpty()) {
                                System.out.println("[AVISO] Essa playlist está vazia!");
                            } else {
                                player.iniciarReproducao(usuario, selecionada, catalogoGlobal);
                            }
                        } else {
                            System.out.println("[ERRO] Playlist invalida!");
                        }
                    }
                    case 8 -> {
                        System.out.print("\nDigite o nome da nova playlist: ");
                        String novoNome = scanner.nextLine();
                        Playlist novaManual = new Playlist(novoNome);
                        minhasPlaylists.add(novaManual);
                        playlistUsuario = novaManual;
                        System.out.println("-> Playlist '" + novoNome + "' criada com sucesso e definida como a atual para edição!");
                    }
                    case 9 -> {
                        System.out.println("\n--- ESCOLHA QUAL PLAYLIST VOCÊ QUER EDITAR/GERENCIAR ---");
                        for (int i = 0; i < minhasPlaylists.size(); i++) {
                            System.out.printf("%d. %s\n", (i + 1), minhasPlaylists.get(i).getNome());
                        }
                        System.out.print("Digite o numero da playlist: ");
                        int escolhaEditar = Integer.parseInt(scanner.nextLine());

                        if (escolhaEditar > 0 && escolhaEditar <= minhasPlaylists.size()) {
                            playlistUsuario = minhasPlaylists.get(escolhaEditar - 1);
                            System.out.println("-> Agora a playlist atual é: '" + playlistUsuario.getNome() + "'!");
                        } else {
                            System.out.println("[ERRO] Opção invalida!");
                        }
                    }
                    case 0 -> {
                        sessaoAtiva = false;
                        System.out.println("\nFazendo logout de " + usuario.getNome() + "...");
                    }
                    case 10 -> {
                        sessaoAtiva = false;
                        sistemaLigado = false;
                        System.out.println("\nEncerrando a aplicação... Até logo!");
                    }
                    default -> System.out.println("[ERRO] Opcao invalida!");
                }
            }
        }
        scanner.close();
    }

    // MEGA CATÁLOGO DE MÍDIAS ATUALIZADO
    private static List<Midia> carregarCatalogoDiversificado() {
        List<Midia> catalogo = new ArrayList<>();

        // -------------------------
        // ÁLBUNS E MÚSICAS DE ROCK
        // -------------------------
        Album albumRock1 = new Album("Toxicity", "Rock");
        Faixa r1 = new Faixa("Chop Suey!", 210.0, 2001, "System of a Down");
        Faixa r2 = new Faixa("Toxicity", 219.0, 2001, "System of a Down");
        albumRock1.adicionarFaixa(r1); albumRock1.adicionarFaixa(r2);

        Album albumRock2 = new Album("Crazy World", "Rock");
        Faixa r3 = new Faixa("Wind of Change", 312.0, 1990, "Scorpions");
        Faixa r4 = new Faixa("Rock You Like a Hurricane", 255.0, 1984, "Scorpions");
        albumRock2.adicionarFaixa(r3); albumRock2.adicionarFaixa(r4);

        Album albumRock3 = new Album("Nevermind", "Rock");
        Faixa r5 = new Faixa("Smells Like Teen Spirit", 301.0, 1991, "Nirvana");
        Faixa r6 = new Faixa("Come As You Are", 219.0, 1991, "Nirvana");
        albumRock3.adicionarFaixa(r5); albumRock3.adicionarFaixa(r6);

        Album albumRock4 = new Album("Back in Black", "Rock");
        Faixa r7 = new Faixa("Back in Black", 255.0, 1980, "AC/DC");
        albumRock4.adicionarFaixa(r7);

        Faixa r8 = new Faixa("Bohemian Rhapsody", 354.0, 1975, "Queen"); // Sem álbum listado

        // -------------------------
        // ÁLBUNS E MÚSICAS DE POP
        // -------------------------
        Album albumPop1 = new Album("Midnight Memories", "Pop");
        Faixa p1 = new Faixa("Story of My Life", 245.0, 2013, "One Direction");
        Faixa p2 = new Faixa("What Makes You Beautiful", 200.0, 2011, "One Direction");
        albumPop1.adicionarFaixa(p1); albumPop1.adicionarFaixa(p2);

        Album albumPop2 = new Album("Thriller", "Pop");
        Faixa p3 = new Faixa("Billie Jean", 294.0, 1982, "Michael Jackson");
        Faixa p4 = new Faixa("Beat It", 258.0, 1982, "Michael Jackson");
        albumPop2.adicionarFaixa(p3); albumPop2.adicionarFaixa(p4);

        Album albumPop3 = new Album("The Fame Monster", "Pop");
        Faixa p5 = new Faixa("Bad Romance", 294.0, 2009, "Lady Gaga");
        albumPop3.adicionarFaixa(p5);

        Faixa p6 = new Faixa("Uptown Funk", 270.0, 2014, "Bruno Mars"); // Sem álbum listado

        // -------------------------
        // ÁLBUNS E MÚSICAS DE SAMBA
        // -------------------------
        Album albumSamba1 = new Album("Deixa a Vida Me Levar", "Samba");
        Faixa s1 = new Faixa("Deixa a Vida Me Levar", 270.0, 2002, "Zeca Pagodinho");
        Faixa s2 = new Faixa("OGUM", 290.0, 1995, "Zeca Pagodinho");
        albumSamba1.adicionarFaixa(s1); albumSamba1.adicionarFaixa(s2);

        Album albumSamba2 = new Album("Verde Que Te Quero Rosa", "Samba");
        Faixa s3 = new Faixa("Preciso Me Encontrar", 178.0, 1976, "Cartola");
        Faixa s4 = new Faixa("O Mundo e um Moinho", 158.0, 1976, "Cartola");
        albumSamba2.adicionarFaixa(s3); albumSamba2.adicionarFaixa(s4);

        Album albumSamba3 = new Album("Alerta Geral", "Samba");
        Faixa s5 = new Faixa("Nao Deixe o Samba Morrer", 267.0, 1975, "Alcione");
        albumSamba3.adicionarFaixa(s5);

        Faixa s6 = new Faixa("Malandro", 245.0, 1979, "Jorge Aragao");

        // -------------------------
        // PODCASTS
        // -------------------------
        Episodio e1 = new Episodio("Tia Lu Podcasts", 120.0, 2026, "Aulinha de Java");
        Episodio e2 = new Episodio("NerdCast RPG - O Segredo de Cleiton", 7200.0, 2021, "Jovem Nerd");
        Episodio e3 = new Episodio("Podpah #500 - Episodio Especial", 9000.0, 2023, "Igao e Mitico");
        Episodio e4 = new Episodio("Flow Podcast - Entrevista do Ano", 10800.0, 2022, "Igor 3K");

        // -------------------------
        // AUDIOBOOKS
        // -------------------------
        Audiobook a1 = new Audiobook("OAC", 1000.4, 1998, "Stallings", "Computação");
        Audiobook a2 = new Audiobook("Harry Potter e a Pedra Filosofal - Cap 1", 1500.0, 1997, "J.K. Rowling", "Fantasia");
        Audiobook a3 = new Audiobook("O Pequeno Príncipe - Cap 1", 900.0, 1943, "Antoine de Saint-Exupery", "Infantil");
        Audiobook a4 = new Audiobook("Habitos Atomicos - Introducao", 1200.0, 2018, "James Clear", "Desenvolvimento Pessoal");


        // --- ADICIONANDO TUDO AO CATÁLOGO GLOBAL ---
        catalogo.add(r1); catalogo.add(r2); catalogo.add(r3); catalogo.add(r4);
        catalogo.add(r5); catalogo.add(r6); catalogo.add(r7); catalogo.add(r8);

        catalogo.add(p1); catalogo.add(p2); catalogo.add(p3); catalogo.add(p4);
        catalogo.add(p5); catalogo.add(p6);

        catalogo.add(s1); catalogo.add(s2); catalogo.add(s3); catalogo.add(s4);
        catalogo.add(s5); catalogo.add(s6);

        catalogo.add(e1); catalogo.add(e2); catalogo.add(e3); catalogo.add(e4);

        catalogo.add(a1); catalogo.add(a2); catalogo.add(a3); catalogo.add(a4);

        return catalogo;
    }
}