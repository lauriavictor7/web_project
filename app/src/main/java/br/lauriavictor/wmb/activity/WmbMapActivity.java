package br.lauriavictor.wmb.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.lauriavictor.wmb.R;
import br.lauriavictor.wmb.model.CustomWindowViewAdapter;
import br.lauriavictor.wmb.model.DatabaseController;
import br.lauriavictor.wmb.model.PlaceAutoCompleteAdapter;
import br.lauriavictor.wmb.model.PlaceInfo;
import br.lauriavictor.wmb.model.User;

public class WmbMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "WmbMapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));

    private AutoCompleteTextView mSearchText;
    private ImageView mLocation, mInfo, mPlacePicker, mFavoritePlace;

    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private PlaceInfo mPlaceInfo;
    private Marker mMarker;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmb_map);

        mSearchText = (AutoCompleteTextView) findViewById(R.id.edtSearch);
        mLocation = (ImageView) findViewById(R.id.ic_gps);
        mInfo = (ImageView) findViewById(R.id.place_info);
        mPlacePicker = (ImageView) findViewById(R.id.place_picket);
        mFavoritePlace = (ImageView) findViewById(R.id.ic_fav);

        getLocationPermission();
    }

    //Initializing search field
    private void init() {
        Log.d(TAG, "init: iniciando");

        //AutoComplete Adapter to line 92
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mSearchText.setOnItemClickListener(mAutoCompleteClickListener);

        mPlaceAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient, LAT_LNG_BOUNDS, null);
        mSearchText.setAdapter(mPlaceAutoCompleteAdapter);

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    //Executando método de busca
                    geoLocate();
                }
                return false;
            }
        });
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicou no icone de gps");
                getDeviceLocation();
            }
        });

        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicou na informação do lugar");
                try {
                    if(mMarker.isInfoWindowShown()) {
                        mMarker.hideInfoWindow();
                    } else {
                        Log.d(TAG, "onClick: informações: " + mPlaceInfo.toString());
                        mMarker.showInfoWindow();
                    }
                } catch (NullPointerException e) {
                    Log.d(TAG, "onClick: NullPointerException: " + e.getMessage());
                }
            }
        });

        //Setting Place Picker
        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(WmbMapActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.d(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.d(TAG, "onClick: GooglePlayServicesRepairableException: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        //Adding place to favorites
        mFavoritePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseController databaseController;
                try {
                    databaseController = new DatabaseController(getBaseContext());
                    //PlaceInfo placeInfo = new PlaceInfo();
                    mPlaceInfo.setName(mPlaceInfo.getName());
                    mPlaceInfo.setAddress(mPlaceInfo.getAddress());
                    mPlaceInfo.setPhoneNumber(mPlaceInfo.getPhoneNumber());
                    mPlaceInfo.setRating(mPlaceInfo.getRating());
                    databaseController.insertPlace(mPlaceInfo);
                    Log.d(TAG, "validatePlace: lugar cadastrado.");
                    Toast.makeText(getApplicationContext(), "Lugar salvo! ", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d(TAG, "validateUser: lugar não cadastrado");
                    e.printStackTrace();
                }
            }
        });
        //searchNearby();
        hideKeyboard();
    }

    //Starting Place Picker
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId());
                placeBufferPendingResult.setResultCallback(mUpdatePlacesDetailsCallback);

            }
        }
    }

    //Research method
    private void geoLocate() {
        Log.d(TAG, "geoLocate: buscando localização");
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(WmbMapActivity.this);
        List<Address> listResult = new ArrayList<>();
        try {
            listResult = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }
        //Se tiver uma lista de endereços printa o endereço pesquisado
        if(listResult.size() > 0) {
            Address address = listResult.get(0);
            Log.d(TAG, "geoLocate: endereço encontrado: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }
    }

    //Making sure the map is ready to be rendered and displayed
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "O mapa está pronto!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onMapReady: o mapa está pronto");
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }
    }

    //Getting the current device location
    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: obtendo a localização atual do dispositivo");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful() && task.getResult() != null) {
                            Log.d(TAG, "onComplete: localização encontrada!");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "Minha localização");

                        } else {
                            Log.d(TAG, "onComplete: localização não encontrada.");
                            Toast.makeText(WmbMapActivity.this, "Não pode encontrar a localização atual.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    //Moving camera and adding a marker with location information
    private void moveCamera(LatLng latLng, float zoom, PlaceInfo placeInfo) {
        Log.d(TAG, "moveCamera: movendo a camera para: lat: " + latLng.latitude + ", long: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        mMap.clear(); //Limpa todos os marcadores do mapa

        mMap.setInfoWindowAdapter(new CustomWindowViewAdapter(WmbMapActivity.this));

        if(placeInfo != null) {
            try {
                String snippet = "Endereço: " +  placeInfo.getAddress() + "\n" +
                                 "Telefone: " +  placeInfo.getPhoneNumber() + "\n" +
                                 "Site: " +  placeInfo.getWebsiteUri() + "\n" +
                                 "Avaliação: " +  placeInfo.getRating() + "\n";
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(placeInfo.getName()).snippet(snippet);
                mMarker = mMap.addMarker(markerOptions);
            } catch (NullPointerException e) {
                Log.d(TAG, "moveCamera: NullPointerException: " + e.getMessage()    );
            }
        } else {
            mMap.addMarker(new MarkerOptions().position(latLng));
        }
        hideKeyboard();
    }

    //Moving the camera and adding a marker in place
    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "moveCamera: movendo a camera para: lat: " + latLng.latitude + ", long: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        //Se no marker não contiver "Minha localização" de titulo, adiciona um marker
        if(!title.equals("Minha localização")) {
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(markerOptions);
            hideKeyboard();
        }
        hideKeyboard();
    }

    //Initializing map
    private void initMap() {
        Log.d(TAG, "initMap: iniciando mapa.");
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(WmbMapActivity.this);
    }

    //Getting permissions on the device
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: obtendo permissões de localização.");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //Getting permissions on the device
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: chamando...");
        mLocationPermissionGranted = false;
        switch(requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if(grantResults.length > 0) {
                    for(int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permissão negada.");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permissão concedida.");
                    mLocationPermissionGranted = true;
                    //starting the map, because the permissions were all granted
                    initMap();
                }
            }
        }
    }

    //To hide the keyboard
    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * Google Places API (Autocomplete sugestions)
     */

    //Go to location when you click on autocomplete suggestion
    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideKeyboard();
            final AutocompletePrediction item = mPlaceAutoCompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            PendingResult<PlaceBuffer> placeBufferPendingResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeBufferPendingResult.setResultCallback(mUpdatePlacesDetailsCallback);
        }
    };

    //Returns details about the places
    private ResultCallback<PlaceBuffer> mUpdatePlacesDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()) {
                Log.d(TAG, "onResult: impossível obter o lugar " + places.getStatus().toString());
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                //The object is being stored in an object of type PlaceInfo, because if it were not, it would get lost because of places.release()
                mPlaceInfo = new PlaceInfo();
                mPlaceInfo.setName(place.getName().toString());
                Log.d(TAG, "onResult: name " + place.getName());
                mPlaceInfo.setAddress(place.getAddress().toString());
                Log.d(TAG, "onResult: Adress " + place.getAddress());
                mPlaceInfo.setId(place.getId().toString());
                Log.d(TAG, "onResult: Id " + place.getId());
                mPlaceInfo.setLatLng(place.getLatLng());
                Log.d(TAG, "onResult: latlng " + place.getLatLng());
                mPlaceInfo.setRating(place.getRating());
                Log.d(TAG, "onResult: rating " + place.getRating());
                mPlaceInfo.setPhoneNumber(place.getPhoneNumber().toString());
                Log.d(TAG, "onResult: number " + place.getPhoneNumber());
                mPlaceInfo.setWebsiteUri(place.getWebsiteUri());
                Log.d(TAG, "onResult: uri " + place.getWebsiteUri());

                Log.d(TAG, "onResult: " + mPlaceInfo.toString());

                Log.d(TAG, "onResult: passou por aqui1");
                DatabaseController databaseController = new DatabaseController(getApplicationContext());
                databaseController.insertPlace(mPlaceInfo);
                Log.d(TAG, "onResult: passou por aqui 2.");
                
            } catch (NullPointerException e) {
                Log.d(TAG, "onResult: NullPointerException: " + e.getMessage());
            }
            //Moving the camera for the selected location
            moveCamera(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude), DEFAULT_ZOOM, mPlaceInfo);
            places.release(); //usa-se release para liberar e poupar memória
        }
    };

    // Search for restaurants nearby
    private void searchNearby() {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=pubs");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
