package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import br.com.ufrpe.foodguru.Mesa.GUI.AdicionarMesaActivity;
import br.com.ufrpe.foodguru.Mesa.GUI.MesasFragment;
import br.com.ufrpe.foodguru.Prato.GUI.AdicionarPratoActivity;
import br.com.ufrpe.foodguru.Prato.GUI.PratosFragment;
import br.com.ufrpe.foodguru.Prato.GUI.SessaoActvity;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.infraestrutura.utils.SPUtil;
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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_estabelecimento);

        toolbar = (Toolbar)findViewById(R.id.toolbarHomeEstabelecimento);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



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

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Helper.criarToast (this, "Pressione VOLTAR denovo para sair");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
        //exibirConfirmacaoSair();
    }

    //menu da action bar (sair)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal_est, menu);

        MenuItem pratoItem = menu.findItem(R.id.menu_action_bar_adicionar_prato);
        Drawable newIconPrato = (Drawable)pratoItem.getIcon();
        newIconPrato.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        pratoItem.setIcon(newIconPrato);

        MenuItem mesaItem = menu.findItem(R.id.menu_action_bar_adicionar_mesa);
        Drawable newIconMesa = (Drawable)mesaItem.getIcon();
        newIconMesa.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        mesaItem.setIcon(newIconMesa);

        MenuItem sessaoItem = menu.findItem(R.id.gerenciar_sessoes);
        Drawable newIconSessao = (Drawable)sessaoItem.getIcon();
        newIconSessao.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        sessaoItem.setIcon(newIconSessao);

        MenuItem configuracoesItem = menu.findItem(R.id.configuracoes_est);
        Drawable newIconConfiguracoes = (Drawable)configuracoesItem.getIcon();
        newIconConfiguracoes.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        configuracoesItem.setIcon(newIconConfiguracoes);

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
        if(id == R.id.gerenciar_sessoes){
            abrirTelaSessao();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void abrirTelaSessao() {
        Intent intent = new Intent(this, SessaoActvity.class);
        startActivity(intent);
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
                SharedPreferences preferences = SPUtil.getSharedPreferences(HomeEstabelecimentoActivity.this);
                //ZERANDO
                SPUtil.putString(preferences,getString(R.string.acc_type_key), "");
                Helper.criarToast(HomeEstabelecimentoActivity.this,"Até mais");
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
