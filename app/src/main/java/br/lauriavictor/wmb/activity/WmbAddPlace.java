package br.lauriavictor.wmb.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.DatabaseController;
import br.lauriavictor.wmb.model.PlaceInfo;

public class WmbAddPlace extends AppCompatActivity {

    private static final String TAG = "WmbAddPlace";
    
    private Button mButtoAddPlace;
    private EditText mPlaceName, mPlaceAddress, mPlacePhone, mPlaceRating;
    private DatabaseController databaseController;
    private PlaceInfo placeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmb_add_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mButtoAddPlace = (Button) findViewById(R.id.buttonAddPlace);
        mPlaceName = (EditText) findViewById(R.id.editTextPname);
        mPlaceAddress = (EditText) findViewById(R.id.editTextPaddress);
        mPlacePhone = (EditText) findViewById(R.id.editTextPphone);
        mPlaceRating = (EditText) findViewById(R.id.editTextPrating);


        mButtoAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    databaseController = new DatabaseController(getApplicationContext());
                    placeInfo = new PlaceInfo();

                    placeInfo.setName(mPlaceName.getText().toString());
                    placeInfo.setAddress(mPlaceAddress.getText().toString());
                    placeInfo.setPhoneNumber(mPlacePhone.getText().toString());
                    placeInfo.setRating(mPlaceRating.getText().length());

                    databaseController.insertPlace(placeInfo);
                    Log.d(TAG, "onClick: passou por aqui e cadastrou.");
                    Toast.makeText(WmbAddPlace.this, "Lugar cadastrado!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d(TAG, "onClick: caiu na exception.");
                    Toast.makeText(WmbAddPlace.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
