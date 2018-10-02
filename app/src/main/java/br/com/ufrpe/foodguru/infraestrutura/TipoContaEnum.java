package br.com.ufrpe.foodguru.infraestrutura;

public enum TipoContaEnum {
    ESTABELECIMENTO("estabelecimento"), CLIENTE("cliente");
    private String tipo;

    TipoContaEnum(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
