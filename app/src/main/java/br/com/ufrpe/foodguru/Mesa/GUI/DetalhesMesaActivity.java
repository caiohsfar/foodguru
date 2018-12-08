package br.com.ufrpe.foodguru.Mesa.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class DetalhesMesaActivity extends AppCompatActivity {
    private ImageView imvQrCode;
    private EditText tvCodigoMesa, tvNumeroMesa;
    private String codigoMesaIntent, numeroMesaIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_mesa);
        codigoMesaIntent = getIntent().getStringExtra("CODIGO_MESA");
        numeroMesaIntent = getIntent().getStringExtra("NUMERO_MESA");
        findViews();
        setUpViews();
        gerarQrCode();
    }

    public void findViews(){
        imvQrCode = findViewById(R.id.imv_qr_code_detalhe);
        tvCodigoMesa = findViewById(R.id.tv_codigo_detalhe);
        tvNumeroMesa = findViewById(R.id.tv_numero_mesa_detalhe);
    }
    public void setUpViews(){
        tvCodigoMesa.setText("Código: " + codigoMesaIntent);
        tvNumeroMesa.setText("Número: " + numeroMesaIntent);
        tvCodigoMesa.setKeyListener( null );
        tvNumeroMesa.setKeyListener( null );
    }

    public void gerarQrCode(){
        imvQrCode.setImageBitmap(Helper.gerarQrCode(codigoMesaIntent));
    }

}
