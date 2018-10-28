package br.com.ufrpe.foodguru.infraestrutura.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumoAdapter;

public class MyCountDownTimer extends CountDownTimer {
    private Context context;
    private TextView cronometro;
    private long timeInFuture;


    public MyCountDownTimer(Context context, TextView cronometro, long timeInFuture, long interval){
        super(timeInFuture, interval);
        this.context = context;
        this.cronometro = cronometro;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timeInFuture = millisUntilFinished;
        cronometro.setText(getCorrectTimer(true,millisUntilFinished)+":"+getCorrectTimer(false,millisUntilFinished));
    }

    @Override
    public void onFinish() {
        timeInFuture -=1000;
        cronometro.setText("00:00");
        Toast.makeText(context,"Finish",Toast.LENGTH_LONG).show();
    }

    private String getCorrectTimer(boolean isMinute, long millisUntilFinished){
        String aux;
        int constCalendar = isMinute ? Calendar.MINUTE : Calendar.SECOND;
        Calendar c =  Calendar.getInstance();
        c.setTimeInMillis(millisUntilFinished);
        aux= c.get(constCalendar)<10 ? "0" +c.get(constCalendar): ""+c.get(constCalendar);
        return (aux);
    }
}
