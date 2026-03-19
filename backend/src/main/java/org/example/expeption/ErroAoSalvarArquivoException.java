package org.example.expeption;


public class ErroAoSalvarArquivoException extends Exception {

    public ErroAoSalvarArquivoException(String message) {
        super(message);
    }

    public ErroAoSalvarArquivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
