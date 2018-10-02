package br.com.ufrpe.foodguru.infraestrutura;

import com.google.firebase.auth.FirebaseAuth;

public final class FirebaseHelper {
    public static final String REFERENCIA_USUARIOS = "Usuarios";
    public static final String REFERENCIA_ESTABELECIMENTO = "Estabelecimentos";
    public static final String REFERENCIA_CLIENTE = "Clientes";


    public FirebaseHelper(){
    }
    public static FirebaseAuth getFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

    public static String getUidUsuario(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
