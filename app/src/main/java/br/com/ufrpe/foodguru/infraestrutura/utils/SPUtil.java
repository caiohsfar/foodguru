package br.com.ufrpe.foodguru.infraestrutura.utils;

import android.content.Context;
import android.content.SharedPreferences;
public class SPUtil {

    public static SharedPreferences getSharedPreferences(Context mContext){
        return mContext.getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
    }
    public static void putString(SharedPreferences preferences, String key, String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }



}
