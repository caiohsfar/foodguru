package br.com.ufrpe.foodguru.Prato.dominio;

import android.os.Parcel;
import android.os.Parcelable;

public class SessaoCardapio implements Parcelable {
    public String id;
    public String nome;
    public String idEstabelecimento;
    public SessaoCardapio(){

    }
    public SessaoCardapio(String nome){
        this.nome = nome;
    }

    protected SessaoCardapio(Parcel in) {
        id = in.readString();
        nome = in.readString();
        idEstabelecimento = in.readString();
    }

    public static final Creator<SessaoCardapio> CREATOR = new Creator<SessaoCardapio>() {
        @Override
        public SessaoCardapio createFromParcel(Parcel in) {
            return new SessaoCardapio(in);
        }

        @Override
        public SessaoCardapio[] newArray(int size) {
            return new SessaoCardapio[size];
        }
    };

    public String getIdEstabelecimento() { return idEstabelecimento; }

    public void setIdEstabelecimento(String idEstabelecimento) { this.idEstabelecimento = idEstabelecimento; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return getNome();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nome);
        dest.writeString(idEstabelecimento);
    }
}
