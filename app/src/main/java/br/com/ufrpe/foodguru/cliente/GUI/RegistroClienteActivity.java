package br.com.ufrpe.foodguru.cliente.GUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.FirebaseDatabase;

import br.com.ufrpe.foodguru.cliente.dominio.Cliente;
import br.com.ufrpe.foodguru.cliente.negocio.ClienteServices;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.utils.TipoContaEnum;
import br.com.ufrpe.foodguru.usuario.GUI.LoginActivity;
import br.com.ufrpe.foodguru.usuario.negocio.UsuarioServices;

public class RegistroClienteActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText nomeClienteReg;
    private EditText emailClienteReg;
    private EditText senhaClienteReg;
    private EditText confirmaSenhaClienteReg;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth = FirebaseHelper.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);
        nomeClienteReg = findViewById(R.id.etNomeCliente);
        emailClienteReg = findViewById(R.id.etEmailCliente);
        senhaClienteReg = findViewById(R.id.etSenhaCliente);
        confirmaSenhaClienteReg = findViewById(R.id.etConfirmarSenhaCliente);
        progressDialog = new ProgressDialog(RegistroClienteActivity.this);
        progressDialog.setTitle("Registrando...");
        progressDialog.setCanceledOnTouchOutside(false);

    }

    private boolean validarCampos(){
        boolean validacao = true;
        if (nomeClienteReg.getText().toString().trim().isEmpty()){
            nomeClienteReg.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if ((!Helper.verificaExpressaoRegularEmail(emailClienteReg.getText().toString())) ||
                emailClienteReg.getText().toString().trim().length() == 0) {
            emailClienteReg.setError(getString(R.string.sp_excecao_email));
            validacao = false;
        }
        if (senhaClienteReg.getText().toString().isEmpty()) {
            senhaClienteReg.setError(getString(R.string.sp_excecao_senha));
            validacao = false;
        }
        if (confirmaSenhaClienteReg.getText().toString().isEmpty()) {
            confirmaSenhaClienteReg.setError(getString(R.string.alerta_campo_vazio));
            validacao = false;
        }
        if (!senhaClienteReg.getText().toString()
                .equals(confirmaSenhaClienteReg.getText().toString())) {
            senhaClienteReg.setError(getString(R.string.sp_excecao_senhas_iguais));
            confirmaSenhaClienteReg.setError(getString(R.string.sp_excecao_senhas_iguais));
            validacao = false;
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
                            if(adicionarEstabelecimento(criarCliente())){
                                setNomeUsuario();
                                progressDialog.dismiss();
                                Helper.criarToast(getApplicationContext(), "Registrado com sucesso.");
                                abrirTelaLogin();
                            }else{
                                progressDialog.dismiss();
                                progressDialog.setCanceledOnTouchOutside(true);
                                Helper.criarToast(RegistroClienteActivity.this
                                        , task.getException().toString());
                            }
                        }
                    }
                });


    }
    public void setNomeUsuario(){
        UsuarioServices usuarioServices = new UsuarioServices();
        usuarioServices.alterarNome(nomeClienteReg.getText().toString());
    }
    public void abrirTelaLogin(){
        Intent intent = new Intent(RegistroClienteActivity.this, LoginActivity.class);
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
            senhaClienteReg.setError("Digite uma senha com no mínimo 6 caracteres");
            confirmaSenhaClienteReg.setError("Digite uma senha com no mínimo 6 caracteres");
        }catch (FirebaseAuthInvalidCredentialsException e){
            emailClienteReg.setError("Informe um email válido");
        }catch (FirebaseAuthUserCollisionException e){
            emailClienteReg.setError("Uma conta com esse email já existe");
        }catch (Exception e){
            Helper.criarToast(RegistroClienteActivity.this,e.toString());
        }
        return verificador;
    }
    public Cliente criarCliente(){
        Cliente cliente = new Cliente();
        cliente.setNome(nomeClienteReg.getText().toString());
        return cliente;
    }
    public boolean adicionarEstabelecimento(Cliente cliente){
       ClienteServices clienteServices = new ClienteServices();
        return clienteServices.adicionarCliente(cliente);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirmar_cadastro_cliente:
                confirmarCadastro(emailClienteReg.getText().toString(),
                        senhaClienteReg.getText().toString());
                break;
            default:
                break;
        }
    }
}
