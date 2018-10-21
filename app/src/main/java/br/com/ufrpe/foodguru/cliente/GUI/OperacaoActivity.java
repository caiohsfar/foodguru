package br.com.ufrpe.foodguru.cliente.GUI;

import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class OperacaoActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseHelper.getFirebaseAuth();
    private BottomNavigationView mNavOperacao;
    private FrameLayout mFrameOperacao;
    private CardapioFragment cardapioFragment;
    private ContaFragment contaFragment;
    private Mesa mesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operacao);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_operacao);

        cardapioFragment = new CardapioFragment();
        setFragment(cardapioFragment);
        contaFragment = new ContaFragment();

        mesa = getIntent().getExtras().getParcelable("mesa");
        mFrameOperacao = (FrameLayout) findViewById(R.id.frame_operacao);
        mNavOperacao = (BottomNavigationView) findViewById(R.id.nav_operacao);

        mNavOperacao.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_cardapio:
                        setFragment(cardapioFragment);
                        return true;

                    case R.id.nav_carrinho:
                        //setFragment(contaFragment);
                        return true;

                    case R.id.nav_conta:
                        setFragment(contaFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!SessaoConsumo.getInstance().getConsumo().getListaItens().isEmpty()){
            Helper.criarToast(this, "Finalize a conta antes de sair.");
            return;
        }
        super.onBackPressed();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_operacao, fragment);
        fragmentTransaction.commit();
    }
}
