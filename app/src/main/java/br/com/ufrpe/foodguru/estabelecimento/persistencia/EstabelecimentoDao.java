package br.com.ufrpe.foodguru.estabelecimento.persistencia;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;

public class EstabelecimentoDao {
    public void adicionaEstabelecimento(Estabelecimento estabelecimento){
        DatabaseReference dao = FirebaseDatabase.getInstance().getReference();
        dao.child("estabelecimento").push().setValue(estabelecimento);

    }

}
