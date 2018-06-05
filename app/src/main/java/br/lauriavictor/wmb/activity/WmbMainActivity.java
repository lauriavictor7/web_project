package br.lauriavictor.wmb.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.InputStream;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.fragment.MainFragment;

public class WmbMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    EditText edtName, edtEmail, edtPW;
    ImageView imgUser;
    //ListView listUser;
    Button addUser;
    Uri imgUri = Uri.parse("android.resource://br.lauriavictor/drawable/ic_user_grey_24dp");
    boolean camera;
    private Bitmap bitmap;
    private static final String TAG = "WmbMainActivity";
    private static final int ERRO_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmb_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        edtName = (EditText) findViewById(R.id.editTextName);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtPW = (EditText) findViewById(R.id.editTextPw);
        //listUser = (ListView) findViewById(R.id);
        //imgUser = (ImageView) findViewById(R.id.ivUser);
        addUser = (Button) findViewById(R.id.buttonAdd2);
    }

    public void init() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.nav_mPlaces);
        drawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WmbMainActivity.this, WmbMapActivity.class);
                startActivity(intent);
            }
        });
    }


    //checando versão do google services
    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checando versão do google services");
        int avaliable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(WmbMainActivity.this);
        if(avaliable == ConnectionResult.SUCCESS) {
            //O usuário pode utilizar o mapa
            Log.d(TAG, "isServicesOK: o Google Play Service está funcionando");
            return true;
        } else if(GoogleApiAvailability.getInstance().isUserResolvableError(avaliable)) {
            //Um erro aconteceu mas podemos resolver (por ser um problema de versão)
            Log.d(TAG, "isServicesOK: aconteceu um erro mas podemos resovler.");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(WmbMainActivity.this, avaliable, ERRO_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Mapa não pode ser utilizado", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void uploadImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select contact image"), 1);
    }

    public void takePhoto(View view) {
        camera = true;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(camera) {
            super.onActivityResult(requestCode, resultCode, data);
            InputStream inputStream = null;
            if(requestCode == 0 && resultCode == RESULT_OK) {
                try {
                    if(bitmap != null) {
                        bitmap.recycle();
                    }
                    inputStream = getContentResolver().openInputStream(data.getData());
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imgUser.setImageBitmap(resizeImage(this, bitmap, 200, 120));
                    imgUser.setRotation(90);
                    imgUri = data.getData();
                    imgUser.setImageURI(data.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            if(resultCode == RESULT_OK) {
                if(requestCode == 1) {
                    imgUri = data.getData();
                    imgUser.setImageURI(data.getData());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wmb_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {

            MainFragment mainFragment = new MainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, mainFragment);
            fragmentTransaction.commit();


        } else if (id == R.id.nav_mPlaces) {
            /*PlacesFragment placesFragment = new PlacesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, placesFragment);
            fragmentTransaction.commit();*/
            startActivity(new Intent(this, WmbMapActivity.class));

        } else if (id == R.id.nav_user) {
            startActivity(new Intent(this, WmbSaveUserActivity.class));

        } else if (id == R.id.nav_contact) {
            //sendEmail();
           startActivity(new Intent(this, WmbAddPlace.class));

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, WmbAboutActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendEmail() {
        camera = false;
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"wmb@wheresmybeer.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Contato pelo App");
        email.putExtra(Intent.EXTRA_TEXT, "Mensagem automática");

        //Configuring to open only e-mail app's
        email.setType("message/rfc822");

        //createChooser allows the user to choose the app he wants
        startActivity(Intent.createChooser(email, "Enviar com:"));
    }

    private static Bitmap resizeImage(Context context, Bitmap bitmap, float nWidth, float nHeigth) {
        Bitmap nBitmap = null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        float destinyFactor = context.getResources().getDisplayMetrics().density;
        float nW = nWidth * destinyFactor;
        float nH = nHeigth * destinyFactor;

        //Calcula escala em porcentagem do tamanho original para o novo tamanho
        float sW = nW / w;
        float sH = nH / h;

        //Criando uma matrix para manipulação da imagem BitMap
        Matrix matrix = new Matrix();

        //Definindo a proporção da escala para a matrix
        matrix.postScale(sW, sH);

        //Criando o novo BitMap com o novo tamanho
        nBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);

        return nBitmap;
    }
}
