package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Prato;
import br.com.ufrpe.foodguru.Prato.dominio.SessaoCardapio;
import br.com.ufrpe.foodguru.estabelecimento.negocio.PratoServices;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class EditarPratoActivity extends AppCompatActivity {
    private EditText nomePrato, descricaoPrato;
    private Prato pratoSelecionado;
    private PratoServices pratoservices = new PratoServices();
    private ProgressDialog progressDialog;
    private Spinner spinnerEditar;
    private ArrayList<SessaoCardapio> sessoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_prato);
        setUpViews();
    }

    private void setUpViews(){
        String nome = getIntent().getStringExtra("NOME_PRATO");
        progressDialog = new ProgressDialog(this);
        descricaoPrato = findViewById(R.id.etEditarDescricaoPrato);
        descricaoPrato.setText(getIntent().getStringExtra("DESCRICAO_PRATO"));
        nomePrato = findViewById(R.id.etEditarNomePrato);
        nomePrato.setText(nome);
        this.spinnerEditar = findViewById(R.id.spinnerEditaSessao);
    }
    private void setupSpinner(){

        sessoes = new ArrayList();

        ArrayAdapter<SessaoCardapio> adapterSessao = new ArrayAdapter<SessaoCardapio>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,sessoes);
        adapterSessao.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditar.setAdapter(adapterSessao);
    }
    public boolean validarCampos() {
        boolean validacao = true;
        if (nomePrato.getText().toString().trim().isEmpty()) {
            nomePrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if (descricaoPrato.getText().toString().trim().isEmpty()) {
            descricaoPrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }

    public void confirmarEdicaoPrato(View view) {
        if (!validarCampos()){
            return;
        }
        editarPrato();
    }

    public void editarPrato(){
        Prato prato = setPratoSelecionado();
        PratoServices pratoServices = new PratoServices();
        if (pratoServices.editarPrato(prato)){
            Helper.criarToast(this, "Prato editado com sucesso");
            finish();
        }else{
            Helper.criarToast(this, "Erro ao editar prato");
        }
    }

    public Prato setPratoSelecionado(){
        String id = getIntent().getStringExtra("ID_PRATO");
        Prato prato = new Prato();
        prato.setIdPrato(id);
        int posicao=  spinnerEditar.getSelectedItemPosition();
        String idSessao = sessoes.get(posicao).getId();
        prato.setNomePrato(nomePrato.getText().toString());
        prato.setDescricaoPrato(descricaoPrato.getText().toString());
        prato.setIdSessao(idSessao);
        return prato;
    }
}
