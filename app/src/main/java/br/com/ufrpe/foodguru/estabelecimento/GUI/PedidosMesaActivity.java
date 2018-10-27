package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.gui.PedidoAdapter;
import br.com.ufrpe.foodguru.Consumo.negocio.ConsumoServices;
import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Mesa.negocio.MesaServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.infraestrutura.utils.StatusMesaEnum;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.*;
import static br.com.ufrpe.foodguru.infraestrutura.utils.StatusMesaEnum.*;

public class PedidosMesaActivity extends AppCompatActivity {
    private List<ItemConsumo> listaPedidos = new LinkedList<>();
    private Mesa mesa;
    private RecyclerView recyclerViewPedidos;
    private PedidoAdapter adapter;
    private ConsumoServices consumoServices = new ConsumoServices();
    private MesaServices mesaServices = new MesaServices();
    private FloatingActionButton fabContaMesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_mesa);
        mesa = getIntent().getExtras().getParcelable("MESA_PEDIDOS");
        iniciarRecyclerView();
        loadPedidos();
        fabContaMesa = findViewById(R.id.fab_conta_mesa);
        fabContaMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaContaMesa();
            }
        });
    }


    private void abrirTelaContaMesa() {
        if (mesa.getIdConsumoAtual().equals("ND")){
            Helper.criarToast(this, "Mesa vazia");
            return;
        }
        Intent intent = new Intent(PedidosMesaActivity.this, ContaMesaActivity.class);
        intent.putExtra("ID_CONSUMO", mesa.getIdConsumoAtual());
        startActivity(intent);
    }

    public void loadPedidos(){
        getFirebaseReference().child(REFERENCIA_ITEM_CONSUMO).orderByChild("mesa/codigoMesa")
                .equalTo(this.mesa.getCodigoMesa()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaPedidos = ConsumoServices.getPedidos(dataSnapshot);
                if (listaPedidos.isEmpty()){
                    if (mesa.getStatus() == PENDENTE.getTipo()){
                        mesaServices.mudarStatus(mesa, VAZIA.getTipo());
                    }
                }
                adapter.updateData(listaPedidos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void iniciarRecyclerView(){
        recyclerViewPedidos = (RecyclerView) findViewById(R.id.recycler_view_pedido_mesa);
        GridLayoutManager layoutManager = new GridLayoutManager(PedidosMesaActivity.this
                , 1);
        recyclerViewPedidos.setLayoutManager(layoutManager);
        adapter = new PedidoAdapter(PedidosMesaActivity.this,listaPedidos, onClickMenu());
        recyclerViewPedidos.setAdapter(adapter);
    }

    private PedidoAdapter.MenuClickListener onClickMenu(){
        return new PedidoAdapter.MenuClickListener() {
            @Override
            public void marcarComoEntregue(int position) {
                ItemConsumo item = listaPedidos.get(position);
                consumoServices.setItemComoEntregue(item);
                Helper.criarToast(PedidosMesaActivity.this,"Item marcado como entregue");
            }
        };
    }
}
