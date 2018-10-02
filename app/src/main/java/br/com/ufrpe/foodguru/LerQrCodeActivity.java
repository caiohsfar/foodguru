package br.com.ufrpe.foodguru;

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

import br.com.ufrpe.foodguru.infraestrutura.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.Helper;
import br.com.ufrpe.foodguru.infraestrutura.LoginActivity;

public class LerQrCodeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseHelper.getFirebaseAuth();
    private TextView mTextMessage;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private EscanearQrCodeFragment escanearQrCodeFragment;
    private MeusDadosClienteFragment meusDadosClienteFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_qr_code:
                    exibirTelaLerQrCode();
                    return true;
                case R.id.nav_meus_dados:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler_qr_code);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.main_nav);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        escanearQrCodeFragment = new EscanearQrCodeFragment();
        meusDadosClienteFragment = new MeusDadosClienteFragment();
        setFragment(escanearQrCodeFragment);

        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_qr_code:
                        setFragment(escanearQrCodeFragment);
                        return true;

                    case R.id.nav_meus_dados:
                        setFragment(meusDadosClienteFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }


    //menu da action bar (sair)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_action_bar_sair) {
            exibirConfirmacaoSair();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                Helper.criarToast(LerQrCodeActivity.this,"Até mais");
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

    public void exibirTelaLerQrCode(){
        Intent intent = new Intent(this, LerQrCodeActivity.class);
        startActivity(intent);
    }
}
