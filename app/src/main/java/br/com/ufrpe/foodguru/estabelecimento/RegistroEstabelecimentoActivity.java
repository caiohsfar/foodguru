package br.com.ufrpe.foodguru.estabelecimento;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import br.com.ufrpe.foodguru.infraestrutura.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.Helper;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.TipoContaEnum;
import br.com.ufrpe.foodguru.usuario.Usuario;

public class RegistroEstabelecimentoActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etNome, etTelefone, etCidade, etRua
            , etComplemento, etEmail, etSenha, etConfirmarSenha;
    private Spinner spEstado;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth = FirebaseHelper.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_estabelecimento);
        setUpViews();
    }
    private void setUpViews(){
        etNome = findViewById(R.id.etNomeEstabelecimento);
        etSenha = findViewById(R.id.etSenhaEstabelecimento);
        etEmail = findViewById(R.id.etEmailEstabelecimento);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenhaEstabelecimento);
        etTelefone = findViewById(R.id.etTelefoneEstabelecimento);
        etCidade = findViewById(R.id.etCidadeEstabelecimento);
        etComplemento = findViewById(R.id.etComplementoEstabelecimento);
        etRua = findViewById(R.id.etRuaEstabelecimento);
        spEstado = findViewById(R.id.spEstadoEstabelecimento);
        progressDialog = new ProgressDialog(RegistroEstabelecimentoActivity.this);
        progressDialog.setTitle("Registrando...");
    }

    private boolean validarCampos(){
        boolean validacao = true;
        if (etNome.getText().toString().trim().isEmpty()){
            etNome.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if ((!Helper.verificaExpressaoRegularEmail(etEmail.getText().toString())) ||
                etEmail.getText().toString().trim().length() == 0) {
            etEmail.setError(getString(R.string.sp_excecao_email));
            validacao = false;
        }
        if (etCidade.getText().toString().isEmpty()) {
            etCidade.setError(getString(R.string.sp_excecao_senha));
            validacao = false;
        }
        if (etTelefone.getText().toString().isEmpty()) {
            etTelefone.setError(getString(R.string.alerta_campo_vazio));
            validacao = false;
        }
        if (!etSenha.getText().toString()
                .equals(etConfirmarSenha.getText().toString())) {
            etSenha.setError(getString(R.string.sp_excecao_senhas_iguais));
            etConfirmarSenha.setError(getString(R.string.sp_excecao_senhas_iguais));
            validacao = false;
        }
        if (etSenha.getText().toString().length() < 6){
            etSenha.setError("Digite una senha com mais de 6 caracteres.");
            validacao = false;
        }
        if (etRua.getText().toString().isEmpty()) {
            etRua.setError(getString(R.string.alerta_campo_vazio));
            validacao = false;
        }
        if (etTelefone.getText().toString().isEmpty()) {
            etRua.setError(getString(R.string.alerta_campo_vazio));
            validacao = false;
        }
        if (etComplemento.getText().toString().isEmpty()) {
            etComplemento.setError(getString(R.string.alerta_campo_vazio));
            validacao = false;
        }
        if (spEstado.getSelectedItemPosition() == 0){
            validacao = false;
            Helper.criarToast(RegistroEstabelecimentoActivity.this, "Selecione seu estado.");
        }

        return validacao;
    }
    public void confirmarCadastro(String email, String senha){
        if (!validarCampos()){
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            adicionarUsuario();
                        }else{
                            progressDialog.dismiss();
                            Helper.criarToast(RegistroEstabelecimentoActivity.this, "Informe um email válido.");
                        }
                    }
                });
    }

    public void adicionarUsuario(){
        Usuario usuario = criarUsuario();
        FirebaseDatabase.getInstance().getReference(FirebaseHelper.REFERENCIA_USUARIOS)
                .child(FirebaseHelper.getUidUsuario())
                .setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    adicionarEstabelecimento();
                }
            }
        });
    }
    public void adicionarEstabelecimento(){
        Estabelecimento estabelecimento = criarEstabelecimento();
        FirebaseDatabase.getInstance().getReference(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .push()
                .setValue(estabelecimento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Helper.criarToast(RegistroEstabelecimentoActivity.this,"Registro conluído com sucesso.");
                }
            }
        });
    }
    public Estabelecimento criarEstabelecimento(){
        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setTelefone(etTelefone.getText().toString());
        estabelecimento.setEndereco(new Endereco(etRua.getText().toString()
                ,etCidade.getText().toString()
                ,spEstado.getSelectedItem().toString()));
        estabelecimento.setUsuario(criarUsuario());
        return estabelecimento;
    }
    public Usuario criarUsuario(){
        Usuario usuario = new Usuario();
        usuario.setNome(etNome.getText().toString());
        usuario.setTipoConta(TipoContaEnum.ESTABELECIMENTO.getTipo());
        return usuario;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.btConfirmarRegistroEstabelecimento):
                confirmarCadastro(etEmail.getText().toString()
                ,etSenha.getText().toString());
                break;
            default:
                break;
        }
    }
}
