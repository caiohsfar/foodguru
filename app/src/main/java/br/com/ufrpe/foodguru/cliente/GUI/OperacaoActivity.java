package br.com.ufrpe.foodguru.cliente.GUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.Mesa.negocio.MesaServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.*;

public class OperacaoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = getFirebaseAuth();
    private BottomNavigationView mNavOperacao;
    private FrameLayout mFrameOperacao;
    private CardapioFragment cardapioFragment;
    private ContaFragment contaFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operacao);

        toolbar = (Toolbar)findViewById(R.id.toolbarOperacao);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_operacao);

        cardapioFragment = new CardapioFragment();
        setFragment(cardapioFragment, "CARD_FRAG");
        contaFragment = new ContaFragment();
        mFrameOperacao = (FrameLayout) findViewById(R.id.frame_operacao);
        mNavOperacao = (BottomNavigationView) findViewById(R.id.nav_operacao);

        mNavOperacao.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_cardapio:
                        setFragment(cardapioFragment, "CARD_FRAG");
                        return true;

                    case R.id.nav_conta:
                        setFragment(contaFragment, "CONTA_FRAG");
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onBackPressed() {
        if (!SessaoConsumo.getInstance().getConsumo().getListaItens().isEmpty()){
            Helper.criarToast(this, "Finalize a conta antes de sair.");
            return;
        }
        exibirConfirmacaoSair();
    }


    private void setFragment(Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_operacao, fragment, TAG);
        fragmentTransaction.commit();
    }

    public void exibirConfirmacaoSair() {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setTitle("Sair");
        msgBox.setMessage("Você não pediu nada, deseja mesmo sair?");
        setBtnPositivoSair(msgBox);
        setBtnNegativoSair(msgBox);
        msgBox.show();
    }

    public void setBtnPositivoSair(AlertDialog.Builder msgBox){
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                idConsumoAtualNull();
                exibirTelaHomeCliente();
            }
        });
    }
    private void idConsumoAtualNull() {
        MesaServices mesaServices = new MesaServices();
        mesaServices.mudarIdConsumoAtual(SessaoConsumo.getInstance().getConsumo().getMesa(), "ND");
    }

    public void setBtnNegativoSair(AlertDialog.Builder msgBox){
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public void exibirTelaHomeCliente(){
        Intent intent = new Intent(this, HomeClienteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("CONTA_FRAG");
        fragment.onActivityResult(requestCode, resultCode, data);
    }



}
