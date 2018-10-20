package br.com.ufrpe.foodguru.Consumo.dominio;

import java.util.HashMap;
import java.util.Map;
//Identico à sessão do projeto passado porém só com o consumo;
public class SessaoConsumo {
    private final Map<String,Object> values = new HashMap<>();
    private static final SessaoConsumo instance = new SessaoConsumo();

    public static SessaoConsumo getInstance() {
        return instance;
    }
    public Consumo getConsumo(){
        return (Consumo) values.get("sessao.consumo");
    }

    public void setConsumo(Consumo consumo){
        setValue("sessao.consumo",consumo);
    }
    public void setValue(String key, Object value) {
        values.put(key, value);
    }
    public void reset(){
        this.values.clear();
    }
}
