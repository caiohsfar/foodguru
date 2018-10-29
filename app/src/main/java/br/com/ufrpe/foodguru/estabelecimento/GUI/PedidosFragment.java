package br.com.ufrpe.foodguru.estabelecimento.GUI;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.gui.PedidoAdapter;
import br.com.ufrpe.foodguru.Consumo.negocio.ConsumoServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_ITEM_CONSUMO;
import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.getFirebaseReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidosFragment extends Fragment {
    private View inflatedLayout;
    private RecyclerView recyclerViewPedidos;
    private PedidoAdapter adapter;
    private List<ItemConsumo> itemList = new LinkedList<>();
    private ConsumoServices consumoServices;



    public PedidosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedLayout = inflater.inflate(R.layout.fragment_pedidos, container, false);
        iniciarRecyclerView();
        consumoServices = new ConsumoServices();
        loadPedidos();
        // Inflate the layout for this fragment
        return inflatedLayout;
    }
    public void iniciarRecyclerView(){
        recyclerViewPedidos = (RecyclerView) inflatedLayout.findViewById(R.id.recycler_view_pedidos);
        GridLayoutManager layoutManager = new GridLayoutManager(inflatedLayout.getContext()
                , 1);
        recyclerViewPedidos.setLayoutManager(layoutManager);
        adapter = new PedidoAdapter(inflatedLayout.getContext(),itemList, onClickMenu());
        recyclerViewPedidos.setAdapter(adapter);
    }

    public void loadPedidos(){
        getFirebaseReference().child(REFERENCIA_ITEM_CONSUMO)
                .orderByChild("uidEstabelecimento")
                .equalTo(FirebaseHelper.getUidUsuario()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemList = ConsumoServices.getPedidos(dataSnapshot);
                        adapter = new PedidoAdapter(getContext(),itemList,onClickMenu());
                        recyclerViewPedidos.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private PedidoAdapter.MenuClickListener onClickMenu(){
        return new PedidoAdapter.MenuClickListener() {
            @Override
            public void marcarComoEntregue(int position) {
                ItemConsumo item = itemList.get(position);
                consumoServices.setItemComoEntregue(item);
                Helper.criarToast(inflatedLayout.getContext(),"Item marcado como entregue");
            }
            public void iniciarPreparoPrato(int position){
                ItemConsumo item = itemList.get(position);
                ConsumoServices consumoServices = new ConsumoServices();
                item.setInicioPreparo(getHorario());
                consumoServices.adicionarInicioPrepato(item);
            }
        };
    }

    public String  getHorario(){
        Calendar data = Calendar.getInstance();
        String hora = Integer.toString(data.get(Calendar.HOUR_OF_DAY));
        if (hora.length()==1){
            hora = "0" +hora;
        }
        String min = Integer.toString(data.get(Calendar.MINUTE));
        if(min.length()==1){
            min = "0" +min;
        }
        String seg = Integer.toString(data.get(Calendar.SECOND));
        if(seg.length()==1){
            seg = "0" +seg;
        }
        return hora + ":" + min + ":" + seg;
    }



}
