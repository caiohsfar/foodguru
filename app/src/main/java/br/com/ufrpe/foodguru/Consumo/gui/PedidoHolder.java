package br.com.ufrpe.foodguru.Consumo.gui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.ufrpe.foodguru.R;

public class PedidoHolder extends RecyclerView.ViewHolder {

    public TextView quantidade, numMesa,nomePrato;

    public PedidoHolder(View view){
        super(view);
        quantidade = view.findViewById(R.id.txtQuantidade);
        numMesa = view.findViewById(R.id.txtNumeroMesa);
        nomePrato = view.findViewById(R.id.txtNomePrato);
    }


}
