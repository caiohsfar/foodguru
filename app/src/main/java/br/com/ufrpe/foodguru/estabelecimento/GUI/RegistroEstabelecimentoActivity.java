package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.ufrpe.foodguru.estabelecimento.dominio.Endereco;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;
import br.com.ufrpe.foodguru.estabelecimento.negocio.EstabelecimentoServices;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.usuario.GUI.LoginActivity;
import br.com.ufrpe.foodguru.usuario.negocio.UsuarioServices;

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

        setarMascaraTelefone();
        
        progressDialog = new ProgressDialog(RegistroEstabelecimentoActivity.this);
        progressDialog.setTitle("Registrando...");
        progressDialog.setCanceledOnTouchOutside(false);
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
        if (etRua.getText().toString().isEmpty()) {
            etRua.setError(getString(R.string.alerta_campo_vazio));
            validacao = false;
        }
        if (etTelefone.getText().toString().isEmpty()) {
            etRua.setError(getString(R.string.alerta_campo_vazio));
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
                        if(isCombinacaoValida(task)){
                            if(adicionarEstabelecimento(criarEstabelecimento())){
                                setNomeUsuario();
                                progressDialog.dismiss();
                                Helper.criarToast(getApplicationContext(),"Registrado com sucesso.");
                                abrirTelaLogin();
                            }else{
                                progressDialog.dismiss();
                                progressDialog.setCanceledOnTouchOutside(true);
                                Helper.criarToast(RegistroEstabelecimentoActivity.this
                                        , "Database Error");
                            }
                        }
                    }
                });
    }
    public void setNomeUsuario(){
        UsuarioServices usuarioServices = new UsuarioServices();
        usuarioServices.alterarNome(etNome.getText().toString());
    }
    public void abrirTelaLogin(){
        Intent intent = new Intent(RegistroEstabelecimentoActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private boolean isCombinacaoValida(@NonNull Task<AuthResult> task) {
        boolean verificador = true;

        try{
            if (task.isSuccessful()) {
                return true;
            }else {
                verificador = false;
                progressDialog.dismiss();
                progressDialog.setCanceledOnTouchOutside(false);
                throw task.getException();
            }
        }catch (FirebaseAuthWeakPasswordException e){
            etSenha.setError("Digite uma senha com no mínimo 6 caracteres");
            etConfirmarSenha.setError("Digite uma senha com no mínimo 6 caracteres");
        }catch (FirebaseAuthInvalidCredentialsException e){
            etEmail.setError("Informe um email válido");
        }catch (FirebaseAuthUserCollisionException e){
            etEmail.setError("Uma conta com esse email já existe");
        }catch (Exception e){
            Helper.criarToast(RegistroEstabelecimentoActivity.this,"Database error");
        }
        return verificador;
    }
    public boolean adicionarEstabelecimento(Estabelecimento estabelecimento){
        EstabelecimentoServices estabelecimentoServices = new EstabelecimentoServices();
        return estabelecimentoServices.adicionarEstabelecimento(estabelecimento);
    }
    public Estabelecimento criarEstabelecimento(){
        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setTelefone(etTelefone.getText().toString());
        estabelecimento.setEndereco(new Endereco(
                 etCidade.getText().toString()
                ,etRua.getText().toString()
                ,spEstado.getSelectedItem().toString()
                ,etComplemento.getText().toString()));
        return estabelecimento;
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

    private void setarMascaraTelefone(){
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(etTelefone, simpleMaskFormatter);
        etTelefone.addTextChangedListener(maskTextWatcher);
    }
}
