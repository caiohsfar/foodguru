package br.com.ufrpe.foodguru.cardapio.GUI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Prato.dominio.Prato;

public class CardapioAdapter extends RecyclerView.Adapter<CardapioHolder>  {

    private List<Prato> pratos;
    private Context context;
    private OnItemClicked mListener;

    public CardapioAdapter(Context context, List<Prato> pratos,  CardapioAdapter.OnItemClicked onClickListener){
        this.pratos = pratos;
        this.context = context;
        this.mListener = onClickListener;

    }
    @Override
    public CardapioHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prato_list_item, parent, false);

        CardapioHolder holder = new CardapioHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final CardapioHolder holder, final int position) {
        String nome = pratos.get(position).getNomePrato();
        String descricao = pratos.get(position).getDescricaoPrato();
        String urlImagem = pratos.get(position).getUrlImagem();
        holder.nome.setText(nome);
        holder.descricao.setText(descricao);
        downloadImage(urlImagem,holder);
        holder.nome.setTag(holder);

        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position);
                }
            });
        }


    }

    public void downloadImage(String urlImagem, final CardapioHolder holder){
        holder.progressBar.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(urlImagem)
                .resize(300,300)
                .into(holder.urlImagem, new Callback() {
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


    public Prato getItem(int position)
    {
        return pratos.get(position);
    }


public interface OnItemClicked {
    void onItemClick(int position);
}
}
