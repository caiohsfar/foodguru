package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Mesa;
import br.com.ufrpe.foodguru.estabelecimento.negocio.MesaServices;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class EditarMesaActivity extends AppCompatActivity {
    private EditText numeroMesa, codigoMesa;
    private ImageView imageView;
    private Mesa mesaSelecionada;
    private MesaServices mesaServices = new MesaServices();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_mesa);
        setUpViews();
    }
    private void setUpViews(){
        String codigo = getIntent().getStringExtra("CODIGO_MESA");
        progressDialog = new ProgressDialog(this);
        numeroMesa = findViewById(R.id.etEditarNumeroMesa);
        numeroMesa.setText(getIntent().getStringExtra("NUMERO_MESA"));
        codigoMesa = findViewById(R.id.etEditarCodigoMesa);
        codigoMesa.setText(codigo);
        imageView = findViewById(R.id.img_qr_code);
        imageView.setImageBitmap((Helper.gerarQrCode(codigo)));
    }
    public boolean validarCampos() {
        boolean validacao = true;
        if (numeroMesa.getText().toString().trim().isEmpty()) {
            numeroMesa.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if (codigoMesa.getText().toString().trim().isEmpty()) {
            codigoMesa.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }
    public void confirmarEdicao(View view) {
        if (!validarCampos()){
            return;
        }
        validarCodigo();
    }
    public void editarMesa(){
        Mesa mesa = setMesaSelecionada();
        MesaServices mesaServices = new MesaServices();
        if (mesaServices.editarMesa(mesa)){
            Helper.criarToast(EditarMesaActivity.this, "Mesa editada com sucesso");
            finish();
        }else{
            Helper.criarToast(EditarMesaActivity.this, "Erro ao editar mesa");
        }
    }

    public Mesa setMesaSelecionada(){
        String id = getIntent().getStringExtra("ID_MESA");
        Mesa mesa = new Mesa();
        mesa.setIdMesa(id);
        mesa.setCodigoMesa(codigoMesa.getText().toString());
        mesa.setNumeroMesa(numeroMesa.getText().toString());
        mesa.setUidEstabelecimento(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid());
        return mesa;
    }

    public void iniciarProgressDialog(){
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }
    public void pararProgressDialog(){
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.dismiss();
    }

    public void validarCodigo() {
        if (codigoMesa.getText().toString().equals(getIntent().getStringExtra("CODIGO_MESA"))){
            editarMesa();
        }else{
            Query query = FirebaseHelper.getFirebaseReference()
                    .child(FirebaseHelper.REFERENCIA_MESA)
                    .orderByChild("codigoMesa")
                    .equalTo(codigoMesa.getText().toString());
            iniciarProgressDialog();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        editarMesa();
                    } else {
                        pararProgressDialog();
                        codigoMesa.setError("Digite um código inexistente.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
