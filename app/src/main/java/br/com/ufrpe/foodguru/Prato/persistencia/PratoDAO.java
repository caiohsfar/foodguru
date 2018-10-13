package br.com.ufrpe.foodguru.Prato.persistencia;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import br.com.ufrpe.foodguru.Prato.dominio.Prato;
import br.com.ufrpe.foodguru.Prato.dominio.SessaoCardapio;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_PRATO;
import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_SESSAO;

public class PratoDAO {
    private DatabaseReference database = FirebaseHelper.getFirebaseReference();

    public boolean adicionarPrato(Prato prato){
        boolean sucess = true;

        try {
            DatabaseReference df = database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child(REFERENCIA_PRATO).push();
            df.setValue(prato);

        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }

    public boolean removerPrato(Prato prato){
        boolean sucess = true;
        //deletando a imagem do prato do storage
        FirebaseStorage.getInstance().getReferenceFromUrl(prato.getUrlImagem()).delete();
        try {
            database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child(REFERENCIA_PRATO).child(prato.getIdPrato()).setValue(null);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }

    public List<Prato> loadPratos(DataSnapshot dataSnapshot){
        List<Prato> pratos = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {

            Prato prato = ds.getValue(Prato.class);
            prato.setIdPrato(ds.getKey());
            pratos.add(prato);
        }
        return pratos;
    }

    public boolean editarPrato(Prato prato){
        System.out.println("id prato: " + prato.getIdPrato());
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child(REFERENCIA_PRATO).child(prato.getIdPrato()).setValue(prato);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }

    public boolean adicionarSessao(SessaoCardapio sessao){
        boolean sucess = true;

        try {
            DatabaseReference df = database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child(REFERENCIA_SESSAO).push();
            df.setValue(sessao);

        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;


    }

    public boolean removerSessao(SessaoCardapio sessao){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child(REFERENCIA_SESSAO).setValue(null);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }

    public boolean editarSessao(SessaoCardapio sessao){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child(REFERENCIA_SESSAO).child(sessao.getId()).setValue(sessao);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }

    public List<SessaoCardapio> loadSessoes(DataSnapshot dataSnapshot){
        List<SessaoCardapio> sessoes = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {

            SessaoCardapio sessaoCardapio = ds.getValue(SessaoCardapio.class);
            sessaoCardapio.setId(ds.getKey());
            sessoes.add(sessaoCardapio);
        }
        return sessoes;
    }
}
