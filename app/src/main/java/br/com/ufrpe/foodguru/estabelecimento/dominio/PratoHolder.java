package br.com.ufrpe.foodguru.estabelecimento.dominio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.ufrpe.foodguru.R;

public class PratoHolder extends RecyclerView.ViewHolder{
    public TextView nome, descricao;

    public PratoHolder(View view) {
        super(view);
        nome = (TextView) view.findViewById(R.id.tvNomePrato);
        descricao = (TextView) view.findViewById(R.id.tvDescicaoPrato);
    }
}
