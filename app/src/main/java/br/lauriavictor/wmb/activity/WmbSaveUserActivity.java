package br.lauriavictor.wmb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.DatabaseController;
import br.lauriavictor.wmb.model.User;
import br.lauriavictor.wmb.model.Utility;

public class WmbSaveUserActivity extends AppCompatActivity {

    private EditText mName, mEmail, mPassword;
    private ImageView mCamera, mPhoto;
    private Button mButtonAddUser;
    private String userChoosenTask;
    private int option = 0;
    DatabaseController databaseController;

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 100;
    private static final String TAG = "WmbSeveUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmb_save_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mName = (EditText) findViewById(R.id.editTextName);
        mEmail = (EditText) findViewById(R.id.editTextEmail);
        mPassword = (EditText) findViewById(R.id.editTextPw);
        mCamera = (ImageView) findViewById(R.id.ic_cam);
        mButtonAddUser = (Button) findViewById(R.id.buttonAdd2);
        mPhoto = (ImageView) findViewById(R.id.ivPhoto);

        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mButtonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser();
                clear();
            }
        });
    }

    private void selectImage() {
        final String takePhoto = "Tirar foto";
        final String galleryPhoto = "Importar da galeria";
        final CharSequence[] items = {takePhoto, galleryPhoto,
                "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(WmbSaveUserActivity.this);
        builder.setTitle("Obter Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(WmbSaveUserActivity.this);
                if (items[item].equals(takePhoto)) {
                    userChoosenTask = "Take Photo";
                    selectFromCamera();
                } else if (items[item].equals(galleryPhoto)) {
                    userChoosenTask = "Choose from Library";
                    selectFromGallery();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void selectFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void selectFromGallery() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_PHOTO);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            if (option == 0) {
                mPhoto.setImageBitmap(mphoto);
            } else {
                mPhoto.setImageBitmap(mphoto);
            }

        } else if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            if (option == 0) {
                mPhoto.setImageURI(selectedImage);
            } else {
                mPhoto.setImageURI(selectedImage);
            }
        }
    }

    private boolean validateUser() {
        if (mName.getText().toString().trim().equals("")) {
            mName.setError("Informe o seu nome!");
            mName.setHint("Informe o seu nome!");
            mName.requestFocus();
            return false;
        } else if (mEmail.getText().toString().trim().equals("")) {
            mEmail.setError("Informe o seu email!");
            mEmail.setHint("Informe o seu email!");
            mEmail.requestFocus();
            return false;
        } else if (mPassword.getText().toString().trim().equals("")) {
            mPassword.setError("Informe a sua senha!");
            mPassword.setHint("Informe a sua senha!");
            mPassword.requestFocus();
            return false;
        } else if(mPhoto.getDrawable() == null) {
            Toast.makeText(getApplicationContext(), "Não esquece de tirar uma foto para o seu perfil!", Toast.LENGTH_LONG).show();
            mPhoto.requestFocus();
            return false;
        }


        Bitmap bitmap = ((BitmapDrawable) mPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, saida);
        byte[] img = saida.toByteArray();

        try {
            databaseController = new DatabaseController(getBaseContext());
            User user = new User();
            user.setName(mName.getText().toString());
            user.setEmail(mEmail.getText().toString());
            user.setPassword(mPassword.getText().toString());
            user.setPhoto(img);

            databaseController.insertUser(user);
            Log.d(TAG, "validateUser: usuario cadastrado.");
            Toast.makeText(getApplicationContext(), "Usuario cadastrado! ", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d(TAG, "validateUser: usuario nao cadastrado");
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(), "Usuario cadastrado! ", Toast.LENGTH_LONG).show();
        /*Intent intent = new Intent(NovoUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);*/
        return true;
    }

    private void clear() {
        mName.setText(null);
        mEmail.setText(null);
        mPassword.setText(null);
        mName.requestFocus();
        mPhoto.setImageResource(R.drawable.ic_person_black_200dp);
    }
}
