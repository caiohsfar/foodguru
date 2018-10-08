package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class DetalhesMesaActivity extends AppCompatActivity {
    private ImageView imvQrCode;
    private TextView tvCodigoMesa;
    private TextView tvNumeroMesa;
    private String codigoMesaIntent;
    private String numeroMesaIntent;

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
        tvCodigoMesa.setText("Código da Mesa: " + codigoMesaIntent);
        tvNumeroMesa.setText("Número da Mesa: " + numeroMesaIntent);
    }

    public void gerarQrCode(){
        imvQrCode.setImageBitmap(Helper.gerarQrCode(codigoMesaIntent));
    }

}
