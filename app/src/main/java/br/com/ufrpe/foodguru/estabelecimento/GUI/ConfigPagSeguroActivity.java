package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;


/**
 * Shows how the requestAuthorization process works with pagseguro.
 * Simulates an user buying a playstation using your sandbox account.
 */


import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.pagseguro.PagSeguroRequest;
import br.com.ufrpe.foodguru.pagseguro.util.PagSeguroUtil;


public class ConfigPagSeguroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pag_teste);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // starting request process
                PagSeguroRequest pagSeguroRequest = new PagSeguroRequest(ConfigPagSeguroActivity.this);
                String requestXml = pagSeguroRequest.buildRequestXml();
                pagSeguroRequest.request(requestXml);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            // se foi uma tentativa de pagamento
            if(requestCode==PagSeguroRequest.PAG_SEGURO_REQUEST_CODE){
                // exibir confirmação de cancelamento
                final String msg = getString(R.string.transaction_cancelled);
                PagSeguroUtil.showConfirmDialog(this, msg, null);
            }
        } else if (resultCode == RESULT_OK) {
            // se foi uma tentativa de pagamento
            if(requestCode==PagSeguroRequest.PAG_SEGURO_REQUEST_CODE){
                // exibir confirmação de sucesso
                final String msg = getString(R.string.transaction_succeded);
                PagSeguroUtil.showConfirmDialog(this, msg, null);
            }
        }
        else if(resultCode == PagSeguroRequest.PAG_SEGURO_REQUEST_CODE){
            switch (data.getIntExtra(PagSeguroRequest.PAG_SEGURO_EXTRA, 0)){
                case PagSeguroRequest.PAG_SEGURO_REQUEST_SUCCESS_CODE:{
                    final String msg ="Autorização concedida com sucesso!";
                    PagSeguroUtil.showConfirmDialog(this,msg,null);
                    abrirHomeEstabelecimento();
                    break;
                }
                case PagSeguroRequest.PAG_SEGURO_REQUEST_FAILURE_CODE:{
                    final String msg = getString(R.string.transaction_error);
                    PagSeguroUtil.showConfirmDialog(this,msg,null);
                    break;
                }
                case PagSeguroRequest.PAG_SEGURO_REQUEST_CANCELLED_CODE:{
                    final String msg = "Autorização cancelada com sucesso!";
                    PagSeguroUtil.showConfirmDialog(this,msg,null);
                    break;
                }
            }
        }
    }

    private void abrirHomeEstabelecimento() {
        Intent intent = new Intent(ConfigPagSeguroActivity.this, HomeEstabelecimentoActivity.class);
        startActivity(intent);
        finish();
    }
}

