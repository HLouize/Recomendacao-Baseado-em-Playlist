package excecoes;

/**
 * Exceção lançada ao tentar gerar uma recomendação passando como base uma Playlist que não possui nenhum
 * item reproduzível cadastrado.
 */
public class PlaylistVaziaException extends RuntimeException {
    public PlaylistVaziaException(String mensagem) {
        super(mensagem);
    }
}