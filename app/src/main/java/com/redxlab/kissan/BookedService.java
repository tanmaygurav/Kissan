package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class BookedService extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG ="BookedService";

    private GoogleMap mMap;
    private Location location_var;
    private LatLng location_lat_log;
    private Double longitude, latitude;
    private RelativeLayout bookedServiceViews;
    private CardView requestedServiceCard;
    private TextView requestedServiceName,serviceProviderName,SPServiceName;
    private Button cancelService;

    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_service);

        bookedServiceViews=findViewById(R.id.idBookedServiceViews);
        requestedServiceCard=findViewById(R.id.idRequestedServiceCard);
        requestedServiceName=findViewById(R.id.idRequestedServiceName);
        cancelService=findViewById(R.id.idCancelRequestBTN);
        serviceProviderName=findViewById(R.id.idServiceProviderName);
        SPServiceName=findViewById(R.id.idServiceName);

        bookedServiceViews.setVisibility(View.GONE);

        Intent intent = getIntent();
        requestedServiceName.setText("Requested Service : "+intent.getStringExtra("Requested Service"));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestedServiceCard.setVisibility(View.GONE);
                bookedServiceViews.setVisibility(View.VISIBLE);
            }
        }, 3000);
        cancelService.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),HomeScreen.class));
        });
        getFromFirebase();


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

    private void getFromFirebase() {
        //        Firebase init
        firebaseApp= FirebaseApp.getInstance();
        db = FirebaseFirestore.getInstance();

        docRef = db.collection("RequestedService").document("request1");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        data=document.getData();
                        populateViews();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void populateViews() {
        String serviceNameTxt = "serviceName",serviceProviderNameTxt="serviceProviderName";
        SPServiceName.setText(String.valueOf(data.get(serviceNameTxt)));
        serviceProviderName.setText(String.valueOf(data.get(serviceProviderNameTxt)));
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