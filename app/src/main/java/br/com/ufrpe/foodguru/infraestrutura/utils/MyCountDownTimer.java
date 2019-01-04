package br.com.ufrpe.foodguru.infraestrutura.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.Calendar;

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
        cronometro.setText("Faltam " + getCorrectTimer(true,millisUntilFinished) + " minuto(s) para chegar.");
    }

    @Override
    public void onFinish() {
        timeInFuture -= 1000;
        cronometro.setText("Seu pedido já está chegando!");
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
