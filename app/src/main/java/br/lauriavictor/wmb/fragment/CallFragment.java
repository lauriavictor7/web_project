package br.lauriavictor.wmb.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.DatabaseController;
import br.lauriavictor.wmb.model.User;
import br.lauriavictor.wmb.model.Utility;

import static android.app.Activity.RESULT_OK;

public class CallFragment extends Fragment {

    View view;
    private Button mButtoUpdate;
    private TextView mNameUpdate, mEmailUpdate, mPasswordUpdate;
    private ImageView mCameraUpdate, mPhotoUpdate;
    private String userChoosenTask;
    private int option = 0;

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 100;

    public CallFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.call_fragment, container, false);

        mButtoUpdate = (Button) view.findViewById(R.id.buttonUpdate);
        mNameUpdate = (EditText) view.findViewById(R.id.editTextNameUpdate);
        mEmailUpdate = (EditText) view.findViewById(R.id.editTextEmailUpdate);
        mPasswordUpdate = (EditText) view.findViewById(R.id.editTextPwUpdate);
        mCameraUpdate = (ImageView) view.findViewById(R.id.ic_camUpdate);
        mPhotoUpdate = (ImageView) view.findViewById(R.id.ivPhotoUpdate);

        mButtoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser();
                clear();
            }
        });

        mCameraUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            if (option == 0) {
                mPhotoUpdate.setImageBitmap(mphoto);
            } else {
                mPhotoUpdate.setImageBitmap(mphoto);
            }

        } else if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            InputStream imageStream = null;
            try {
                imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            if (option == 0) {
                mPhotoUpdate.setImageURI(selectedImage);
            } else {
                mPhotoUpdate.setImageURI(selectedImage);
            }
        }
    }

    public boolean validateUser() {
        if (mNameUpdate.getText().toString().trim().equals("")) {
            mNameUpdate.setError("Informe o seu nome!");
            mNameUpdate.setHint("Informe o seu nome!");
            mNameUpdate.requestFocus();
            return false;
        } else if (mEmailUpdate.getText().toString().trim().equals("")) {
            mEmailUpdate.setError("Informe o seu email!");
            mEmailUpdate.setHint("Informe o seu email!");
            mEmailUpdate.requestFocus();
            return false;
        } else if (mPasswordUpdate.getText().toString().trim().equals("")) {
            mPasswordUpdate.setError("Informe a sua senha!");
            mPasswordUpdate.setHint("Informe a sua senha!");
            mPasswordUpdate.requestFocus();
            return false;
        }

        Bitmap bitmap = ((BitmapDrawable) mPhotoUpdate.getDrawable()).getBitmap();
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, saida);
        byte[] img = saida.toByteArray();

        try {
            User user = new User();
            user.setId(1);
            user.setName(mNameUpdate.getText().toString());
            user.setEmail(mEmailUpdate.getText().toString());
            user.setPassword(mPasswordUpdate.getText().toString());
            user.setPhoto(img);
            DatabaseController databaseController = new DatabaseController(getContext());
            databaseController.updateUser(user);

            Toast.makeText(getContext(), "Usuário atualizado!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Não foi possível realizar a atualização!", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    public void clear() {
        mNameUpdate.setText(null);
        mEmailUpdate.setText(null);
        mPasswordUpdate.setText(null);
        mNameUpdate.requestFocus();
        mPhotoUpdate.setImageResource(R.drawable.ic_person_black_200dp);
    }

    private void selectImage() {
        final String takePhoto = "Tirar foto";
        final String galleryPhoto = "Importar da galeria";
        final CharSequence[] items = {takePhoto, galleryPhoto,
                "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Obter Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getContext());
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
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
