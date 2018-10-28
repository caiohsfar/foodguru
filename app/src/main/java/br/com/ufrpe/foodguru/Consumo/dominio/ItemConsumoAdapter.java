package br.com.ufrpe.foodguru.Consumo.dominio;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.MyCountDownTimer;

public class ItemConsumoAdapter extends RecyclerView.Adapter<ItemConsumoAdapter.ItemConsumoHolder> {
    private List<ItemConsumo> itemConsumoList;
    private MyCountDownTimer timer;
    private Context context;
    private List<ItemConsumo> listaPedidosEst;

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
    public void onBindViewHolder(@NonNull ItemConsumoHolder holder, int position) {
        String nome = itemConsumoList.get(position).getPrato().getNomePrato();
        String preco = String.valueOf(itemConsumoList.get(position).getPrato().getPreco());
        String quantidade = String.valueOf(itemConsumoList.get(position).getQuantidade());
        holder.quantidade.setText("Quantidade: " + quantidade);
        holder.nome.setText(nome);
        holder.preco.setText("Preço: "+ preco);
        timer = new MyCountDownTimer(context, 1000, percorrer(), holder);
        timer.start();
    }

    public Long percorrer() {
        Long estimativa = Long.valueOf(0);

        for(ItemConsumo itemConsumo : listaPedidosEst) {
            if(itemConsumo.getInicioPreparo() != null) {
                estimativa += itemConsumo.getPrato().getEstimativa() - (diferencaData(itemConsumo.getInicioPreparo(), getHorario()));
            }else {
                estimativa += itemConsumo.getPrato().getEstimativa();
            }
            if(itemConsumo.getIdCliente() == FirebaseHelper.getUidUsuario()){
                break;
            }
        }
        return estimativa;
    }

    public String  getHorario(){
        Calendar data = Calendar.getInstance();
        String hora = Integer.toString(data.get(Calendar.HOUR_OF_DAY));
        if (hora.length() == 1){
            hora = "0" + hora;
        }
        String min = Integer.toString(data.get(Calendar.MINUTE));
        if(min.length() == 1){
            min = "0" + min;
        }
        return hora + ":" + min;
    }

    public static long diferencaData(String horaInicial, String horaFinal) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();
            Calendar calFinal = Calendar.getInstance();
            cal.setTime(sdf.parse(horaInicial));
            calFinal.setTime(sdf.parse(horaFinal));
            long diferenca = (calFinal.getTimeInMillis() - cal.getTimeInMillis());
            return diferenca;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return itemConsumoList.size();
    }

    public void setListaPedidosEst(List<ItemConsumo> listaPedidosEst) {
        this.listaPedidosEst = listaPedidosEst;
    }

    public class ItemConsumoHolder extends RecyclerView.ViewHolder{
        public TextView nome, preco, quantidade, cronometro, fila;


        public ItemConsumoHolder(View view) {
            super(view);
            nome = (TextView) view.findViewById(R.id.tv_nome_item);
            preco = (TextView) view.findViewById(R.id.tv_preco_item);
            quantidade = (TextView) view.findViewById(R.id.tv_quantidade_item);
            cronometro = (TextView) view.findViewById(R.id.tv_cronometro);
            //fila = (TextView) view.findViewById(R.id.);
        }


    }

}
