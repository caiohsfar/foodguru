package br.com.ufrpe.foodguru.cliente.GUI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private TextView cronometro;
    private MyCountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cronometro);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPedidosEst();
        cronometro = findViewById(R.id.cronometro_novo);

    }


    public Long percorrer(LinkedList<ItemConsumo> itensConsumoEst) {
        Long estimativa = Long.valueOf(0);

        for(ItemConsumo itemConsumo : itensConsumoEst) {
            //if(itemConsumo.getInicioPreparo() != null) {
            //    estimativa += itemConsumo.getPrato().getEstimativa() - (diferencaData(itemConsumo.getInicioPreparo(), getHorario()));
            //}else {
            estimativa += itemConsumo.getPrato().getEstimativa();
            //}
            if(itemConsumo.getIdCliente().equals(FirebaseHelper.getUidUsuario())){
                break;
            }
        }
        Toast.makeText(this, estimativa.toString(), Toast.LENGTH_SHORT).show();
        return estimativa;

    }

    public String  getHorario(){
        Calendar data = Calendar.getInstance();
        String hora = Integer.toString(data.get(Calendar.HOUR_OF_DAY));
        if (hora.length() == 1){
            hora = "0" + hora;
        }
        String min = Integer.toString(data.get(Calendar.MINUTE));
        if(min.length() == 1){
            min = "0" + min;
        }
        return hora + ":" + min;
    }

    public static long diferencaData(String horaInicial, String horaFinal) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
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
                        System.out.println(itensConsumoEst.toString());
                        System.out.println(itensConsumoEst.toString());
                        System.out.println(itensConsumoEst.toString());
                        System.out.println(itensConsumoEst.toString());
                        timer = new MyCountDownTimer(CronometroActivity.this, cronometro, percorrer(itensConsumoEst), 1000);
                        timer.start();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    /*
    public void setListaPedidosEst(List<ItemConsumo> listaPedidosEst) {
        this.listaPedidosEst = listaPedidosEst;
    }
    */
}
