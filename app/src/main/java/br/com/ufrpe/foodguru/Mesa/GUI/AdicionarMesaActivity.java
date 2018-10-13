package br.com.ufrpe.foodguru.Mesa.GUI;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Mesa.negocio.MesaServices;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class AdicionarMesaActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etNumeroMesa, etCodigoMesa;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_mesa);
        setUpViews();
    }

    private void setUpViews(){
        findViewById(R.id.btnConfirmarAdicionarMesa).setOnClickListener(this);
        etNumeroMesa = findViewById(R.id.etNumeroMesa);
        etCodigoMesa = findViewById(R.id.etCodigoMesa);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adicionando...");

    }
    public void iniciarProgressDialog(){
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }
    public void pararProgressDialog(){
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.dismiss();
    }


    private boolean validarCampos(){
        boolean validacao = true;
        if (etNumeroMesa.getText().toString().trim().isEmpty()){
            etNumeroMesa.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if (etCodigoMesa.getText().toString().trim().isEmpty()){
            etCodigoMesa.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConfirmarAdicionarMesa:{
                if(!validarCampos()){
                    return;
                }
                validarCodigo();
                break;
            }
        }
    }

    //Tem que ajeitar essa validação (está verificando em todos os nodos "Mesas" se tem um cósigo igual.
    public void validarCodigo(){
        Query query = FirebaseHelper.getFirebaseReference()
                .child(FirebaseHelper.REFERENCIA_MESA)
                .orderByChild("codigoMesa")
                .equalTo(etCodigoMesa.getText().toString());
        iniciarProgressDialog();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    adicionarMesa();
                }else {
                    pararProgressDialog();
                    etCodigoMesa.setError("Digite um código inexistente.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void adicionarMesa() {
        MesaServices mesaServices = new MesaServices();
        if (mesaServices.adicionarMesa(criarMesa())){
            Helper.criarToast(AdicionarMesaActivity.this, "Mesa adicionada com sucesso.");
            pararProgressDialog();
            finish();
        }else{
            pararProgressDialog();
            Helper.criarToast(AdicionarMesaActivity.this, "Erro ao adicionar mesa");
            finish();
        }
    }

    private Mesa criarMesa() {
        return new Mesa(etNumeroMesa.getText().toString()
                ,etCodigoMesa.getText().toString()
                , FirebaseHelper.getUidUsuario());
    }


}
