package br.com.ufrpe.foodguru.cliente.GUI;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Prato.GUI.PratoAdapter;
import br.com.ufrpe.foodguru.Prato.dominio.Prato;
import br.com.ufrpe.foodguru.Prato.dominio.PratoView;
import br.com.ufrpe.foodguru.Prato.dominio.SessaoCardapio;
import br.com.ufrpe.foodguru.Prato.negocio.PratoServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_PRATO;

public class CardapioActivity extends AppCompatActivity {
    private PratoAdapter adapter;
    private RecyclerView mRecyclerView;
    private PratoServices pratoServices = new PratoServices();
    private List<PratoView> pratosViews;
    private Spinner sessao;
    private List<SessaoCardapio> arraySessoes;
    private Mesa mesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
        mesa = getIntent().getExtras().getParcelable("mesa");
        sessao = (Spinner) findViewById(R.id.spinnerSessaoCardapio);
        loadArraySessoes();
        iniciarRecyclerView();
        setCliqueAdapterSessoes();
    }

    public void setCliqueAdapterSessoes(){
        AdapterView.OnItemSelectedListener escolha = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int posicao =  sessao.getSelectedItemPosition();
                if (posicao == 0){
                    loadTodosPratos();
                }else{
                    String idSessao = arraySessoes.get(posicao).getId();
                    loadCardapioBySessao(idSessao);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        sessao.setOnItemSelectedListener(escolha);

    }

    public void loadArraySessoes(){
        FirebaseHelper.getFirebaseReference().child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(mesa.getUidEstabelecimento())
                .child(FirebaseHelper.REFERENCIA_SESSAO)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PratoServices pratoServices = new PratoServices();
                        arraySessoes = pratoServices.loadSessoes(dataSnapshot);
                        arraySessoes.add(0, new SessaoCardapio("Todos os pratos"));
                        setupSpinner();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    public void loadTodosPratos(){
        FirebaseHelper.getFirebaseReference().child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(mesa.getUidEstabelecimento())
                .child(REFERENCIA_PRATO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pratosViews = pratoToPratoView((ArrayList<Prato>)pratoServices.loadPratos(dataSnapshot));
                adapter = new PratoAdapter(CardapioActivity.this,pratosViews);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void loadCardapioBySessao(String idSesao){
        FirebaseHelper.getFirebaseReference().child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(mesa.getUidEstabelecimento())
                .child(REFERENCIA_PRATO).orderByChild("idSessao")
                .equalTo(idSesao)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pratosViews = pratoToPratoView((ArrayList<Prato>)pratoServices.loadPratos(dataSnapshot));
                        adapter = new PratoAdapter(CardapioActivity.this,pratosViews);
                        mRecyclerView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void setupSpinner() {
        ArrayAdapter<SessaoCardapio> adapterSessao = new ArrayAdapter<SessaoCardapio>(this
                ,android.R.layout.simple_spinner_dropdown_item,arraySessoes);
        adapterSessao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sessao.setAdapter(adapterSessao);
    }

    public void iniciarRecyclerView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_cardápio);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new PratoAdapter(this,pratosViews);
        mRecyclerView.setAdapter(adapter);
    }
    private List<PratoView> pratoToPratoView(ArrayList<Prato> pratos){
        ArrayList<PratoView> pratosViews = new ArrayList<>();
        for (Prato prato : pratos) {
            PratoView pratoView = new PratoView();
            pratoView.setPrato(prato);
            pratosViews.add(pratoView);
        }
        return pratosViews;
    }

}
