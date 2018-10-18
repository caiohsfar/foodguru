package br.com.ufrpe.foodguru.cliente.GUI;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;



/**
 * A simple {@link Fragment} subclass.
 */
public class MeusDadosClienteFragment extends Fragment implements View.OnClickListener {
    private StorageReference  storageReference = FirebaseStorage.getInstance().getReference();;
    private EditText tvNome, tvEmail;
    private View viewInflado;
    private ImageView imvFoto;
    private FirebaseUser currentUser = FirebaseHelper.getFirebaseAuth().getCurrentUser();
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALERY_REQUEST_CODE = 71;
    private ProgressDialog progressDialog;
    private ProgressBar finalProgressBar;

    public MeusDadosClienteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflado = inflater.inflate(R.layout.fragment_meus_dados_cliente, container, false);
        finalProgressBar = viewInflado.findViewById(R.id.progress_bar);
        setTextViews();
        setClicks();
        Helper.verificarPermissaoEscrever(getContext(),getActivity());
        carregarFoto();
        progressDialog = new ProgressDialog(viewInflado.getContext());
        return viewInflado;
    }
    private void setClicks(){
        viewInflado.findViewById(R.id.editar_foto).setOnClickListener(this);
    }

    public void setTextViews() {
        tvNome = viewInflado.findViewById(R.id.tvNomeClienteMeusDados);
        tvNome.setText(currentUser.getDisplayName());
        tvEmail = viewInflado.findViewById(R.id.tvEmailClienteMeusDados);
        tvEmail.setText(currentUser.getEmail());
        tvNome.setKeyListener( null );
        tvEmail.setKeyListener( null );
    }

    public void showMenuEscolhaEdicao() {
        BottomSheet.Builder builder = new BottomSheet.Builder(viewInflado.getContext());
        builder.setTitle("Escolha uma opção")
                .setSheet(R.menu.menu_editar_foto)
                .setListener(new BottomSheetListener() {
                    @Override
                    public void onSheetShown(@NonNull BottomSheet bottomSheet, @Nullable Object o) {

                    }

                    @Override
                    public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem, @Nullable Object o) {
                        switch (menuItem.getItemId()) {
                            case R.id.escolher_foto:
                                if (Helper.verificarPermissoesLeitura(getContext(),getActivity())){
                                    escolherFoto();
                                }
                            case R.id.tirar_foto:
                                if (Helper.verificarPermissaoAcessarCamera(getContext(),getActivity())){
                                    tirarFoto();
                                }
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @Nullable Object o, int i) {

                    }
                }).show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editar_foto: {
                showMenuEscolhaEdicao();
                break;
            }
        }
    }


    private void carregarFoto() {
        imvFoto = viewInflado.findViewById(R.id.iv_cliente);
        if (currentUser != null) {
            if (currentUser.getPhotoUrl() != null) {
                finalProgressBar.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(currentUser.getPhotoUrl())
                        .into(imvFoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                finalProgressBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }else{
                finalProgressBar.setVisibility(View.GONE);
            }
        }
    }
    private void tirarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void escolherFoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selecione uma imagem"), GALERY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    tirarFoto();
                }
            }
            case GALERY_REQUEST_CODE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    escolherFoto();
                }
            }
            default:
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALERY_REQUEST_CODE:
                Uri uriFoto;
                if (requestCode == GALERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
                    uriFoto = data.getData();
                    try{
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriFoto);
                        setFotoImageView(bitmap);
                        inserirFoto(uriFoto);
                    }catch(IOException e ){
                        Log.d("IOException upload", e.getMessage());
                    }
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (requestCode == CAMERA_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap bitmap = (Bitmap) extras.get("data");
                            setFotoImageView(bitmap);
                            //transforma em uri pra jogar no storage
                            uriFoto = Helper.getImageUri(viewInflado.getContext(), bitmap);
                            inserirFoto(uriFoto);
                        }
                    }
                }
                break;
                default:
                    break;
        }
    }

    private void setFotoImageView(Bitmap bitmap) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        imvFoto.setImageBitmap(bitmap);
    }
    private void inserirFoto(Uri uriFoto){
        iniciarProgressDialog();
        final StorageReference ref = storageReference.child("images/perfil/" + currentUser.getUid() + ".bmp");
        ref.putFile(uriFoto).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri imageUri = task.getResult();
                    Helper.criarToast(viewInflado.getContext(),imageUri.toString());
                    atualizarFotoUsuario(imageUri);
                    fecharProgressDialog();
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Helper.criarToast(viewInflado.getContext(),"Foto atualizada com sucesso.");
                fecharProgressDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Helper.criarToast(viewInflado.getContext(),e.toString());
                fecharProgressDialog();
            }
        });
    }

    private void iniciarProgressDialog() {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Atualizando...");
        progressDialog.show();
    }

    public void atualizarFotoUsuario(Uri uriImagem){
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uriImagem)
                .build();
        currentUser.updateProfile(profileChangeRequest);
    }
    private void fecharProgressDialog(){
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.dismiss();
    }
}
