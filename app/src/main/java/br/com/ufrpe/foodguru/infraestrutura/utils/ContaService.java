package br.com.ufrpe.foodguru.infraestrutura.utils;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import br.com.ufrpe.foodguru.Consumo.dominio.Consumo;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.R;

public class ContaService extends Service {
    private static final String TAG = "CONSUMO";

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreateService()");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        atualizarConsumo(SessaoConsumo.getInstance().getConsumo());
        super.onTaskRemoved(rootIntent);
    }

    public void atualizarConsumo(Consumo consumo){
        SharedPreferences sharedPreferences = SPUtil.getSharedPreferences(this);
        //se a conta estiver vazia ou tiver sido finalizada, o consumo atual será setado como nulo.. sem itens na conta.
        if (consumo == null){
            Log.d("CONSUMO: ", "Consumo nulo!");
            SPUtil.putString(sharedPreferences, getString(R.string.consumo_atual), null);
        }
        //o cliente tem ítens na conta ou ela não tiver sido finalizada, então temos que salvar o consumo antes dele fechar o app,
        //para quando ele iniciar denovo, os ítens estarem na conta e ele na mesa.
        else{
            Log.d("CONSUMO: ", "Consumo Não nulo!");
            Gson gson = new Gson();
            String consumoJson = gson.toJson(consumo);
            SPUtil.putString(sharedPreferences, getString(R.string.consumo_atual), consumoJson);
        }

    }
}
