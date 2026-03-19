package org.example.expeption;

public class ErroAoCarregarArquivoException extends Exception {
    public ErroAoCarregarArquivoException(String message) {
        super(message);
    }

    public ErroAoCarregarArquivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
