package br.com.ufrpe.foodguru.Prato.GUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import br.com.ufrpe.foodguru.Prato.negocio.PratoServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Prato.dominio.SessaoCardapio;
import br.com.ufrpe.foodguru.estabelecimento.GUI.HomeEstabelecimentoActivity;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class AdicionarSessaoActvity extends AppCompatActivity {
    private EditText etNomeSessao;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_sessao);
        etNomeSessao = (EditText) findViewById(R.id.etNomeSessao);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adicionando...");
    }
    // Falta validar  se já existe uma sessão com o mesmo nome
    public boolean validarCampos(){
        boolean validacao = true;
        if (etNomeSessao.getText().toString().trim().isEmpty()){
            etNomeSessao.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }

    public void adicionarSessao(View view){
        PratoServices pratoServices = new PratoServices();

        if(validarCampos()){
            SessaoCardapio sessaoCardapio = new SessaoCardapio();
            sessaoCardapio.setNome(etNomeSessao.getText().toString());
            sessaoCardapio.setIdEstabelecimento(FirebaseHelper.getUidUsuario());
            if(pratoServices.adicionarSessao(sessaoCardapio)){
                Helper.criarToast(getApplicationContext(),"Sessao adicionada com sucesso");
                telaHomeEstabelecimento();
            }else{
                Helper.criarToast(this, "Erro ao adicionar sessao.");
            }
        }
        etNomeSessao.setText("");

    }
    public void iniciarProgressDialog(){
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }
    public void pararProgressDialog(){
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.dismiss();
    }

    public void telaHomeEstabelecimento() {
        Intent intent = new Intent(this, HomeEstabelecimentoActivity.class);
        startActivity(intent);
    }
}
