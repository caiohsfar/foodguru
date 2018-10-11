package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Prato;
import br.com.ufrpe.foodguru.estabelecimento.negocio.PratoServices;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class AdicionarPratoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNomePrato, etDescricaoPrato;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_prato);
        setUpViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConfirmarAdicionarPrato:{
                if(!validarCampos()){
                    return;
                }
                adicionarPrato();
                break;
            }
        }
    }

    private boolean validarCampos(){
        boolean validacao = true;
        if (etNomePrato.getText().toString().trim().isEmpty()){
            etNomePrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if (etDescricaoPrato.getText().toString().trim().isEmpty()){
            etDescricaoPrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }

    private void setUpViews(){
        findViewById(R.id.btnConfirmarAdicionarPrato).setOnClickListener(this);
        etNomePrato = findViewById(R.id.etNomePrato);
        etDescricaoPrato = findViewById(R.id.etDescricaoPrato);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adicionando...");
    }

    private Prato criarPrato() {
        return new Prato(etNomePrato.getText().toString(), etDescricaoPrato.getText().toString());
    }

    private void adicionarPrato() {
        PratoServices pratoServices = new PratoServices();
        if (pratoServices.adicionarPrato(criarPrato())){
            Helper.criarToast(this, "Prato adicionado com sucesso.");
            finish();
        }else{
            Helper.criarToast(this, "Erro ao adicionar prato");
            finish();
        }
    }
}
