package br.com.ufrpe.foodguru.infraestrutura.utils;

import android.app.Application;
//Biblioteca com um monte de utils
import com.blankj.utilcode.util.Utils;

public class FoodGuru extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
