package excecoes;

/**
 Exceção lançada ao tentar adicionar um usuário a uma instância de
 PlanoFamily que já atingiu o limite máximo de 6 membros.
 */
public class LimiteUsuariosPlanoException extends RuntimeException {
    public LimiteUsuariosPlanoException(String mensagem) {
        super(mensagem);
    }
}