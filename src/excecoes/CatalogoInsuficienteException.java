package excecoes;

/**
 Exceção lançada quando o catálogo de mídias do sistema não possuir o
 número mínimo de itens solicitados para preencher a playlist de recomendação.
 */
public class CatalogoInsuficienteException extends RuntimeException {
    public CatalogoInsuficienteException(String mensagem) {
        super(mensagem);
    }
}