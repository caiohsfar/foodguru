package br.com.ufrpe.foodguru.cardapio.GUI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.ufrpe.foodguru.R;

public class CardapioHolder extends RecyclerView.ViewHolder {
    public TextView nome, descricao, preco;
    public ImageView urlImagem;
    public ProgressBar progressBar;

    public CardapioHolder(@NonNull View view) {
        super(view);
        nome = (TextView) view.findViewById(R.id.tvNomePrato);
        descricao = (TextView) view.findViewById(R.id.tvDescicaoPrato);
        preco = (TextView) view.findViewById(R.id.tv_preco_prato);
        urlImagem = (ImageView) view.findViewById(R.id.ivImagemPrato);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_recycler_prato);
    }
}
