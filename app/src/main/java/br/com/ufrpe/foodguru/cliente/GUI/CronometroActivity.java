package br.com.ufrpe.foodguru.cliente.GUI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumoAdapter;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.Consumo.negocio.ConsumoServices;
import br.com.ufrpe.foodguru.Mesa.GUI.MesaAdapter;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.MyCountDownTimer;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_ITEM_CONSUMO;
import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.getFirebaseReference;

public class CronometroActivity extends AppCompatActivity {
    private TextView cronometro, nomePrato, fila;
    private MyCountDownTimer timer;
    private String nomePratoIntent;
    private String idItem;
    private int contador;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro);

        toolbar = (Toolbar)findViewById(R.id.toolbarHomeCliente);
        setSupportActionBar(toolbar);

        idItem = getIntent().getStringExtra("ID_ITEM");
        nomePratoIntent = getIntent().getStringExtra("NOME_PRATO");
        nomePrato = findViewById(R.id.nome_prato_cro);
        nomePrato.setText(nomePratoIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPedidosEst();
        cronometro = findViewById(R.id.cronometro_novo);

    }


    public Long percorrer(LinkedList<ItemConsumo> itensConsumoEst) {
        Long estimativa = Long.valueOf(0);
        for (ItemConsumo itemConsumo : itensConsumoEst){
            contador += 1;
            estimativa += itemConsumo.getPrato().getEstimativa();
            Log.d("IdItem ", idItem);
            if (itemConsumo.getId().equals(idItem)){
                contador -= 1;
                break;
            }
        }
        Long desconto = diferencaData(itensConsumoEst.get(0).getHoraPedido(), getHorario());


        Log.d("Estimativa geral", String.valueOf(estimativa*60000 - desconto));
        return estimativa*60000 - desconto;

    }

    public String  getHorario(){
        Calendar data = Calendar.getInstance();
        String hora = Integer.toString(data.get(Calendar.HOUR_OF_DAY));
        if (hora.length()==1){
            hora = "0" +hora;
        }
        String min = Integer.toString(data.get(Calendar.MINUTE));
        if(min.length()==1){
            min = "0" +min;
        }
        String seg = Integer.toString(data.get(Calendar.SECOND));
        if(seg.length()==1){
            seg = "0" +seg;
        }
        return hora + ":" + min + ":" + seg;
    }

    public static long diferencaData(String horaInicial, String horaFinal) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            Calendar calFinal = Calendar.getInstance();
            cal.setTime(sdf.parse(horaInicial));
            calFinal.setTime(sdf.parse(horaFinal));
            long diferenca = (calFinal.getTimeInMillis() - cal.getTimeInMillis());
            return diferenca;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void loadPedidosEst(){
        getFirebaseReference().child(REFERENCIA_ITEM_CONSUMO)
                .orderByChild("uidEstabelecimento")
                .equalTo(SessaoConsumo.getInstance().getConsumo().getMesa().getUidEstabelecimento())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        LinkedList<ItemConsumo> itensConsumoEst = (LinkedList<ItemConsumo>) ConsumoServices.getPedidos(dataSnapshot);
                        Collections.sort(itensConsumoEst);
                        contador = 0;
                        fila = findViewById(R.id.fila_cro);
                        timer = new MyCountDownTimer(CronometroActivity.this, cronometro, percorrer(itensConsumoEst), 1000);
                        fila.setText("Há " + String.valueOf(contador) + " pedido(s) na frente do seu.");

                        timer.start();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

    public static String somaHora(String horaInicial,int contador) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(horaInicial));
            cal.add(Calendar.MINUTE, contador);
            String x = sdf.format(cal.getTime());
            return x;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
