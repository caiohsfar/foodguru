package br.com.ufrpe.foodguru.Prato.dominio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.ufrpe.foodguru.R;

public class PratoHolder extends RecyclerView.ViewHolder{
    public TextView nome, descricao, preco;
    public ImageView imagem;
    public ProgressBar progressBar;

    public PratoHolder(View view) {
        super(view);
        nome = (TextView) view.findViewById(R.id.tvNomePrato);
        descricao = (TextView) view.findViewById(R.id.tvDescicaoPrato);
        imagem = (ImageView) view.findViewById(R.id.ivImagemPrato);
        preco = (TextView) view.findViewById(R.id.tv_preco_prato);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar_recycler_prato);
    }
}
