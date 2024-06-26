package br.com.mcm.apimcmfood.domain.exception;

public class ProdutoNaoEncontradaException extends SemStacktraceException{

    private static final long serialVersionUID = 1L;

    public ProdutoNaoEncontradaException(String mensagem){
        super(mensagem);
    }

    public ProdutoNaoEncontradaException(Long restauranteId, Long produtoId) {
        this(String.format("Não existe um cadastro de produto com código %d para o restaurante de código %d",
                produtoId, restauranteId));
    }
}
