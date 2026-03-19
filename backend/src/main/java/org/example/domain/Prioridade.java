package org.example.domain;

public enum Prioridade {
    MINIMA(1),
    BAIXA(2),
    MEDIA(3),
    ALTA(4),
    CRITICA(5);
    private final int valor;

    Prioridade(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
