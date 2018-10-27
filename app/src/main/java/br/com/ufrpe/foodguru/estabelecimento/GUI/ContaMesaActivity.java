package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumoAdapter;
import br.com.ufrpe.foodguru.Consumo.negocio.ConsumoServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_ITEM_CONSUMO;
import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.getFirebaseReference;

public class ContaMesaActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<ItemConsumo> itemConsumoList = new LinkedList<>();
    private ItemConsumoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_mesa);
        iniciarRecyclerView();
        listarItensPedidos();
    }

    public void iniciarRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_conta_mesa);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        //itemConsumoList = consumoAtual.getListaItens();
        adapter = new ItemConsumoAdapter(itemConsumoList);
        mRecyclerView.setAdapter(adapter);
    }
    public void listarItensPedidos(){
        String idConsumo = getIntent().getStringExtra("ID_CONSUMO");
        getFirebaseReference().child(REFERENCIA_ITEM_CONSUMO).orderByChild("idConsumo").equalTo(idConsumo)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        itemConsumoList = ConsumoServices.getPedidosMesa(dataSnapshot);
                        adapter = new ItemConsumoAdapter(itemConsumoList);
                        mRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
