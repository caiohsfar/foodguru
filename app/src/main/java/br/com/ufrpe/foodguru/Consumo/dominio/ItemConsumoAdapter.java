package br.com.ufrpe.foodguru.Consumo.dominio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

import br.com.ufrpe.foodguru.R;

public class ItemConsumoAdapter extends RecyclerView.Adapter<ItemConsumoAdapter.ItemConsumoHolder> {
    private List<ItemConsumo> itemConsumoList;
    private Context context;

    public ItemConsumoAdapter(Context context, List<ItemConsumo> itemConsumoList) {
        this.context = context;
        this.itemConsumoList = itemConsumoList;

    }
    @NonNull
    @Override
    public ItemConsumoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.conta_item_view, viewGroup, false);

        ItemConsumoHolder holder = new ItemConsumoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemConsumoHolder holder, final int position) {
        String nome = itemConsumoList.get(position).getPrato().getNomePrato();
        //String preco = String.valueOf(itemConsumoList.get(position).getPrato().getPreco());
        String preco = "R" + getPrecoFormatado(position);
        /**
         * Se aparecer dois "R" antes do preco, tem que usar a função de baixo
         * String preco = getPrecoFormatado(position).contains("R") ? getPrecoFormatado(position) : "R" + getPrecoFormatado(position);
         *
         **/
        String quantidade = String.valueOf(itemConsumoList.get(position).getQuantidade());
        holder.quantidade.setText("Quantidade: " + quantidade);
        holder.nome.setText(nome);
        holder.preco.setText(preco);
    }
    public String getPrecoFormatado(int position){
        return NumberFormat.getCurrencyInstance()
                .format(itemConsumoList.get(position).getValor())
                .replace(".",",");
    }


    @Override
    public int getItemCount() {
        return itemConsumoList.size();
    }


    public class ItemConsumoHolder extends RecyclerView.ViewHolder{
        public TextView nome, preco, quantidade, cronometro, fila;


        public ItemConsumoHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.tv_nome_item);
            preco = (TextView) view.findViewById(R.id.tv_preco_item);
            quantidade = (TextView) view.findViewById(R.id.tv_quantidade_item);
            //cronometro = (TextView) view.findViewById(R.id.tv_cronometro);
            //fila = (TextView) view.findViewById(R.id.);
        }


    }

}
