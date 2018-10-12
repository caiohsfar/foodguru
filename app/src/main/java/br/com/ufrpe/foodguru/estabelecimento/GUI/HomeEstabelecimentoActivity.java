package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import br.com.ufrpe.foodguru.Mesa.GUI.AdicionarMesaActivity;
import br.com.ufrpe.foodguru.Mesa.GUI.MesasFragment;
import br.com.ufrpe.foodguru.Prato.GUI.AdicionarPratoActivity;
import br.com.ufrpe.foodguru.Prato.GUI.PratosFragment;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.usuario.GUI.LoginActivity;

public class HomeEstabelecimentoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseHelper.getFirebaseAuth();
    private TextView mTextMessage;
    private BottomNavigationView mNavEstabelecimento;
    private FrameLayout mFrameEstabelecimento;
    private PratosFragment pratosFragment;
    private MesasFragment mesasFragment;
    private PedidosFragment pedidosFragment;
    private MeusDadosEstabelecimentoFragment administracaoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_estabelecimento);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.nav_estabelecimento);

        pratosFragment = new PratosFragment();
        mesasFragment = new MesasFragment();
        pedidosFragment = new PedidosFragment();
        administracaoFragment = new MeusDadosEstabelecimentoFragment();

        setFragment(pratosFragment);

        mFrameEstabelecimento = findViewById(R.id.frame_estabelecimento);
        mNavEstabelecimento = findViewById(R.id.nav_estabelecimento);

        mNavEstabelecimento.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_pratos:
                        setFragment(pratosFragment);
                        return true;

                    case R.id.nav_mesas:
                        setFragment(mesasFragment);
                        return true;

                    case R.id.nav_pedidos:
                        setFragment(pedidosFragment);
                        return true;

                    case R.id.nav_meus_dados_est:
                        setFragment(administracaoFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_estabelecimento, fragment);
        fragmentTransaction.commit();
    }


    //menu da action bar (sair)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_est, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.menu_action_bar_adicionar_prato) {
            telaAdicionarPrato();
            return true;
        }
        if (id == R.id.menu_action_bar_adicionar_mesa) {
            telaAdicionarMesa();
            return true;
        }
        if (id == R.id.menu_action_bar_editar_perfil_est) {
            telaEditarDadosEstabelecimento();
            return true;
        }
        if (id == R.id.menu_action_bar_sair_est) {
            exibirConfirmacaoSair();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void telaAdicionarPrato() {
        Intent intent = new Intent(this, AdicionarPratoActivity.class);
        startActivity(intent);
    }

    public void telaAdicionarMesa() {
        Intent intent = new Intent(this, AdicionarMesaActivity.class);
        startActivity(intent);
    }

    public void telaEditarDadosEstabelecimento() {
        Intent intent = new Intent(this, EditarDadosEstabelecimentoActivity.class);
        startActivity(intent);
    }

    public void exibirConfirmacaoSair() {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setTitle("Sair");
        msgBox.setMessage("Deseja mesmo sair?");
        setBtnPositivoSair(msgBox);
        setBtnNegativoSair(msgBox);
        msgBox.show();
    }

    public void setBtnPositivoSair(AlertDialog.Builder msgBox){
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Helper.criarToast(HomeEstabelecimentoActivity.this,"Até mais");
                FirebaseAuth.getInstance().signOut();
                exibirTelaLogin();
                finish();
            }
        });
    }

    public void setBtnNegativoSair(AlertDialog.Builder msgBox){
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public void exibirTelaLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
