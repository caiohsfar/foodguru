package br.com.ufrpe.foodguru.Prato.GUI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

import br.com.ufrpe.foodguru.R;


import br.com.ufrpe.foodguru.Prato.dominio.PratoHolder;
import br.com.ufrpe.foodguru.Prato.dominio.PratoView;

public class PratoAdapter extends RecyclerView.Adapter<PratoHolder>{
    private final Context context;
    private List<PratoView> pratos;
    private final PratoAdapter.PratoOnClickListener onClickListener;

    public interface PratoOnClickListener {
        void onClickPrato(PratoHolder holder, int indexPrato);
        void onLongClickPrato(PratoHolder holder, int indexPrato);
    }
    public PratoAdapter(Context context, List<PratoView> pratos,PratoAdapter.PratoOnClickListener onClickListener) {
        this.pratos = pratos;
        this.context = context;
        this.onClickListener = onClickListener;
    }
    public PratoAdapter(Context context, List<PratoView> pratos) {
        this.pratos = pratos;
        this.context = context;
        this.onClickListener = null;
    }

    @Override
    public PratoHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prato_list_item, parent, false);

        PratoHolder holder = new PratoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PratoHolder holder, final int position) {
        String nome = pratos.get(position).getPrato().getNomePrato();
        String descricao = pratos.get(position).getPrato().getDescricaoPrato();
        String urlImagem = pratos.get(position).getPrato().getUrlImagem();
        String preco = NumberFormat.getCurrencyInstance().format(pratos.get(position).getPrato().getPreco());
        holder.nome.setText(nome);
        holder.descricao.setText(descricao);
        holder.preco.setText(preco);
        downloadImage(urlImagem,holder);
        holder.nome.setTag(holder);

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickPrato(holder, position);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onClickListener.onLongClickPrato(holder, position);
                    return true;
                }
            });
        }
        int corFundo = context.getResources().getColor(pratos.get(position).isSelecionado() ? R.color.EscuroFoodGuruTransparente
                : R.color.zxing_transparent);
        holder.itemView.setBackgroundColor(corFundo);
    }

    public void downloadImage(String urlImagem, final PratoHolder holder){
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(urlImagem)
                .resize(300,300)
                .into(holder.imagem, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return this.pratos != null ? this.pratos.size() : 0;
    }

}
