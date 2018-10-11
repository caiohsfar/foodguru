package br.com.ufrpe.foodguru.usuario.persistencia;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseException;

import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

public class UsuarioDAO {
    private FirebaseAuth mAuth = FirebaseHelper.getFirebaseAuth();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    public boolean setNome(String nome) {
        boolean isValido = true;
        try {
            if (currentUser != null) {
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nome)
                        .build();
                currentUser.updateProfile(profileChangeRequest);
            }
        }catch (DatabaseException e){
            isValido = false;
        }
        return isValido;
    }
    public boolean setEmail(String email){
        boolean isValido = true;
        if (currentUser!= null) {
           try{
               currentUser.updateEmail(email);
           }catch (DatabaseException e){
                isValido = false;

            }
        }
        return isValido;
    }

}
