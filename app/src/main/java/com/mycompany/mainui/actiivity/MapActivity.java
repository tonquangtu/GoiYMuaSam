package com.mycompany.mainui.actiivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycompany.mainui.R;
import com.mycompany.mainui.map.CheckGPS;
import com.mycompany.mainui.map.CustomWindowAdapter;
import com.mycompany.mainui.map.InfoWindow;
import com.mycompany.mainui.map.MyApiEndPointInterface;
import com.mycompany.mainui.map.SpinMarker;
import com.mycompany.mainui.map.TaskLoading;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String BASE_URL = "https://maps.googleapis.com/";
    public Gson gson;
    public Retrofit retrofit;

    private GoogleMap mMap;
    private MyApiEndPointInterface apiService;

    private InfoWindow infoWindow;
    private String myLocation = "ml";
    private boolean start = false;
    View tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // get intent
        infoWindow = (InfoWindow) getIntent().getSerializableExtra("Info Window");

        // init layout
        tab = findViewById(R.id.tab);
        TextView name = (TextView)findViewById(R.id.name_store);
        name.setText(infoWindow.getName());
        TextView address = (TextView)findViewById(R.id.address_store);
        address.setText(infoWindow.getAddress());
        Button direct = (Button)findViewById(R.id.direct_btn);
        direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check gps
                CheckGPS.check(MapActivity.this);
                start = true;
            }
        });

        // init retrofit
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(MyApiEndPointInterface.class);

        // init map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // setup map
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setInfoWindowAdapter(new CustomWindowAdapter(this,getLayoutInflater(), infoWindow));

        // add marker
        final LatLng position = new LatLng(infoWindow.getLat(), infoWindow.getLng());
        // move camera
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position)
                .bearing(-90f).tilt(30f).zoom(13f).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        final Marker marker = mMap.addMarker(new MarkerOptions()
                .position(position));
        SpinMarker.dropPinEffect(marker);



        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(myLocation.equals("ml") && start == true){
                    tab.setVisibility(View.INVISIBLE);
                    marker.hideInfoWindow();
                    myLocation = location.getLatitude() + "," + location.getLongitude();
                    TaskLoading taskLoading = new TaskLoading(apiService, mMap, myLocation
                            , infoWindow.getLat() + "," + infoWindow.getLng());
                    taskLoading.execute();
                }
            }
        });

    }


}
