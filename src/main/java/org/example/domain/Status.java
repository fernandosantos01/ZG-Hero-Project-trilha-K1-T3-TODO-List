package org.example.domain;

public enum Status {
    A_FAZER(1),
    EM_ANDAMENTO(2),
    CONCLUIDO(3);

    private int valor;

    Status(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
