package br.com.ufrpe.foodguru.Prato.GUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ufrpe.foodguru.Prato.negocio.PratoServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Prato.dominio.SessaoCardapio;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_PRATO;

public class SessaoActvity extends AppCompatActivity implements View.OnClickListener{
    private ListView listViewSessao;
    private ArrayAdapter<SessaoCardapio> adapter;
    private ArrayList<SessaoCardapio> arraySessoes;
    private DatabaseReference database = FirebaseHelper.getFirebaseReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessao);
        setupViews();
        loadArraySessoes();

    }
    private void setupViews(){
        findViewById(R.id.btn_adicionar_sessao).setOnClickListener(this);
        listViewSessao = findViewById(R.id.listViewSessao);
        clickListView();

    }
    public void clickListView(){
        listViewSessao.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(Menu.NONE,1, Menu.NONE, "Deletar" );
                menu.add(Menu.NONE,2, Menu.NONE, "Editar" );
            }
        });

    }
    public void loadArraySessoes(){
        FirebaseHelper.getFirebaseReference().child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(FirebaseHelper.getUidUsuario())
                .child(FirebaseHelper.REFERENCIA_SESSAO)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PratoServices pratoServices = new PratoServices();
                        arraySessoes = (ArrayList<SessaoCardapio>) pratoServices.loadSessoes(dataSnapshot);
                        iniciarListView();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void iniciarListView() {
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, arraySessoes);
        listViewSessao.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int posicao = menuInfo.position;
        switch (item.getItemId()){
            case 1:{
                exibirAlertDialog(posicao);
                break;
            }
            case 2:{
                abrirTelaEditarSessao(posicao);
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void abrirTelaEditarSessao(int posicao) {
        Intent intent = new Intent(this, EditarSessaoActvity.class);
        intent.putExtra("sessao", arraySessoes.get(posicao));
        startActivity(intent);
    }

    public void deletarSessao(int posicao){
        PratoServices pratoServices = new PratoServices();
        SessaoCardapio sessaoCardapio = arraySessoes.get(posicao);
        if (pratoServices.removerSessao(sessaoCardapio)){
            Helper.criarToast(this, "Categoria removida com sucesso.");
            adapter.notifyDataSetChanged();
            deletarPratos(sessaoCardapio);
        }else{
            Helper.criarToast(this, "Erro ao remover sessão");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_adicionar_sessao:{
                abrirTelaAdicionarSessao();
                break;
            }
        }
    }

    private void abrirTelaAdicionarSessao() {
        Intent intent = new Intent(this, AdicionarSessaoActvity.class);
        startActivity(intent);
    }
    public void exibirAlertDialog(int posicao){
        AlertDialog.Builder builderDeletar = setarMensagemDeletar();
        setarBotaoNegativoDeletar(builderDeletar);
        setarBotaoPositivoDeletar(builderDeletar, posicao);
        exibirTextoConfirmarDelecao(builderDeletar);

    }
    public AlertDialog.Builder setarMensagemDeletar(){
        AlertDialog.Builder  builder = new AlertDialog.Builder(
                this);
        builder.setMessage("Ao deletar a sessão, todos os pratos pertencentes à ela serão deletados.");
        return builder;
    }

    public void setarBotaoNegativoDeletar(AlertDialog.Builder builderDeletar){
        builderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    public void exibirTextoConfirmarDelecao(AlertDialog.Builder builderDeletar){
        AlertDialog alert =  builderDeletar.create();
        alert.setTitle("Tem certeza que deseja remover esta sessão?");
        alert.show();
    }
    public void setarBotaoPositivoDeletar(AlertDialog.Builder builderDeletar, final int posicao){
        builderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                deletarSessao(posicao);
            }
        });
    }
    public void deletarPratos(SessaoCardapio sessao){
        database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                .child(REFERENCIA_PRATO)
                .orderByChild("idSessao")
                .equalTo(sessao.getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    ds.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
