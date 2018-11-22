package br.com.ufrpe.foodguru.Mesa.dominio;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.ufrpe.foodguru.infraestrutura.utils.StatusMesaEnum;

@SuppressLint("ParcelCreator")
public class Mesa implements Parcelable {
    private String numeroMesa;
    private String codigoMesa;
    private int status = StatusMesaEnum.VAZIA.getTipo();
    private String uidEstabelecimento;
    private String idConsumoAtual = "ND";

    public Mesa(){
    }

    public Mesa(String numeroMesa, String codigoMesa, String uidEstabelecimento, String idConsumoAtual) {
        this.numeroMesa = numeroMesa;
        this.codigoMesa = codigoMesa;
        this.uidEstabelecimento = uidEstabelecimento;
        this.idConsumoAtual = idConsumoAtual;
    }

    protected Mesa(Parcel in) {
        numeroMesa = in.readString();
        codigoMesa = in.readString();
        uidEstabelecimento = in.readString();
        status = in.readInt();
        idConsumoAtual = in.readString();
    }

    public static final Creator<Mesa> CREATOR = new Creator<Mesa>() {
        @Override
        public Mesa createFromParcel(Parcel in) {
            return new Mesa(in);
        }

        @Override
        public Mesa[] newArray(int size) {
            return new Mesa[size];
        }
    };

    public String getUidEstabelecimento() {
        return uidEstabelecimento;
    }

    public void setUidEstabelecimento(String uidEstabelecimento) {
        this.uidEstabelecimento = uidEstabelecimento;
    }

    public String getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(String numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getCodigoMesa() {
        return codigoMesa;
    }

    public void setCodigoMesa(String codigoMesa) {
        this.codigoMesa = codigoMesa;
    }

    public String getIdConsumoAtual() {
        return idConsumoAtual;
    }

    public void setIdConsumoAtual(String idConsumoAtual) {
        this.idConsumoAtual = idConsumoAtual;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(numeroMesa);
        dest.writeString(codigoMesa);
        dest.writeString(uidEstabelecimento);
        dest.writeInt(status);
        dest.writeString(idConsumoAtual);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
