package br.com.ufrpe.foodguru.infraestrutura.utils;

public enum StatusMesaEnum {
    PENDENTE(0), OCUPADA(1), VAZIA(2);
    private int tipo;

    StatusMesaEnum(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }
}
