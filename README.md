# Sistema de Recomendação Baseado em Playlist

Sistema desenvolvido em Java para gerenciamento de playlists e recomendação de músicas e outros tipos de mídia.

## Sobre o projeto

O sistema simula uma plataforma de streaming, permitindo que usuários criem playlists, reproduzam mídias e recebam recomendações baseadas no conteúdo de suas playlists.

O projeto foi desenvolvido com foco em Programação Orientada a Objetos e na utilização de padrões de projeto.

## Funcionalidades

- Criação e gerenciamento de playlists
- Reprodução de músicas e outras mídias
- Sistema de recomendação baseado em similaridade
- Recomendação baseada na última mídia da playlist
- Sistema de reprodução automática
- Histórico de reprodução
- Filtro para evitar recomendações repetidas
- Inserção de propagandas para usuários gratuitos
- Diferentes tipos de planos
- Gerenciamento de usuários
- Exceções personalizadas

## Estratégias de recomendação

### PlaylistSimilarityStrategy

Recomenda mídias com base nas tags presentes na playlist do usuário.

### PlaylistAutoCompleteStrategy

Utiliza a última mídia da playlist para encontrar conteúdos relacionados, considerando informações como artista, álbum, podcast ou assunto.

## Planos

O sistema possui diferentes tipos de planos:

- Gratuito
- Individual
- Estudante
- Family

Cada plano possui suas próprias regras, como inserção de propagandas e limite de usuários.

## Tecnologias

- Java
- JUnit 5
- Programação Orientada a Objetos
- Strategy Pattern

## Estrutura do projeto

    src/
    ├── classesDeMidia/
    ├── excecoes/
    ├── filtros/
    ├── organizacao/
    ├── planos/
    ├── principais/
    ├── recomendacao/
    └── testes/

## Testes

Os testes automatizados estão localizados em:

    src/testes/RecommendationEngineTest.java

Eles verificam funcionalidades relacionadas ao sistema de recomendação, filtros, propagandas, playlists e planos.

## Execução

A classe principal do projeto está localizada em:

    src/principais/Main.java

Para executar o projeto, utilize uma IDE compatível com Java e execute a classe `Main`.

## Objetivo

O projeto foi desenvolvido com finalidade acadêmica para aplicar conceitos de Programação Orientada a Objetos, padrões de projeto, tratamento de exceções, coleções e testes automatizados.
