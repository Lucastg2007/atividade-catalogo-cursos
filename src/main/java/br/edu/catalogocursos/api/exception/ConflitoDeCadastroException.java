package br.edu.catalogocursos.api.exception;

public class ConflitoDeCadastroException extends RuntimeException {

    public ConflitoDeCadastroException(String mensagem) {
        super(mensagem);
    }
}
