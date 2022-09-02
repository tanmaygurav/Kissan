package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class BookedService extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG ="BookedService";

    private GoogleMap mMap;
    private Location location_var;
    private LatLng location_lat_log;
    private Double longitude, latitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_service);

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        try {
            mapFragment.getMapAsync(this);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Location Error : "+e,Toast.LENGTH_SHORT );
        }


        //        getting location permissions -- in app permissions
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(BookedService.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            location_var = location;
                            location_lat_log = new LatLng(latitude, longitude);
//                  next line to get the blue circle
                            if (ActivityCompat.
                                    checkSelfPermission(BookedService.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.
                                    checkSelfPermission(BookedService.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                            moveCameratoLocation(location_lat_log);
                        }
                    }
                });
    }

    private void moveCameratoLocation(LatLng location_lat_log) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location_lat_log)
                .zoom(18).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
}