package br.com.ufrpe.foodguru.Prato.GUI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.com.ufrpe.foodguru.Prato.dominio.SessaoCardapio;
import br.com.ufrpe.foodguru.Prato.negocio.PratoServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class EditarSessaoActvity extends AppCompatActivity implements View.OnClickListener{
    private SessaoCardapio sessaoSelecionada;
    private EditText etNomeSessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_sessao);
        sessaoSelecionada = getIntent().getExtras().getParcelable("sessao");
        setUpViews();

    }
    private void setUpViews(){
       findViewById(R.id.btnConfirmaEditarSessao).setOnClickListener(this);
       etNomeSessao = findViewById(R.id.etNomeSessaoedt);
       etNomeSessao.setText(sessaoSelecionada.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirmaEditarSessao:{
                if (!validarCampos()){
                    break;
                }
                validarSessao();
            }
        }
    }
    private void setSessaoSelecionada(){
        sessaoSelecionada.setNome(etNomeSessao.getText().toString());
    }

    private void editarSessao() {
        PratoServices pratoServices = new PratoServices();
        if (pratoServices.editarSessao(sessaoSelecionada)){
            Helper.criarToast(this, "Sessão editada com sucesso.");
            finish();
        }else{
            Helper.criarToast(this, "Erro ao editar Sessão");
        }
    }
    private boolean validarCampos() {
        boolean validacao = true;
        if (etNomeSessao.getText().toString().trim().isEmpty()) {
            etNomeSessao.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }
    private void validarSessao(){
        if (sessaoSelecionada.getNome().equals(etNomeSessao.getText().toString())){
            Helper.criarToast(this, "Sessão editada com sucesso.");
            finish();
            return;
        }
        setSessaoSelecionada();
        FirebaseHelper.getFirebaseReference().child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(FirebaseHelper.getUidUsuario())
                .child(FirebaseHelper.REFERENCIA_SESSAO)
                .orderByChild("nome")
                .equalTo(sessaoSelecionada.getNome())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    editarSessao();
                }else{
                    Helper.criarToast(EditarSessaoActvity.this, "Já existe uma sessão com esse nome.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
