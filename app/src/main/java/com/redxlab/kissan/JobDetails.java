package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

public class JobDetails extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG ="JobNotification";
    private TextView farmerName,contactNumber,jobLocation,serviceName,pricePerHour;
    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data;

    private GoogleMap mMap;
    private Location location_var;
    private LatLng location_lat_log;
    private Double longitude, latitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        farmerName=findViewById(R.id.idFarmerName);
        contactNumber=findViewById(R.id.idJobContactNumber);
        jobLocation=findViewById(R.id.idJobLocation);
        serviceName=findViewById(R.id.idServiceName);
        pricePerHour=findViewById(R.id.idJobPrice);

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
                .addOnSuccessListener(JobDetails.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            location_var = location;
                            location_lat_log = new LatLng(latitude, longitude);
//                  next line to get the blue circle
                            if (ActivityCompat.
                                    checkSelfPermission(JobDetails.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED
                                    && ActivityCompat.
                                    checkSelfPermission(JobDetails.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mMap.setMyLocationEnabled(true);
                            moveCameratoLocation(location_lat_log);
                        }
                    }
                });
    }

    private void populateViews() {
        String serviceNameTxt = "serviceName",locationTxt="village";
        String perHourTxt="pricePerHour",farmerNameTxt="farmerName";
        String contactNumberTxt="contactNumber";
        farmerName.setText(String.valueOf(data.get(farmerNameTxt)));
        contactNumber.setText(String.valueOf(data.get(contactNumberTxt)));
        serviceName.setText(String.valueOf(data.get(serviceNameTxt)));
        jobLocation.setText(String.valueOf(data.get(locationTxt)));
        pricePerHour.setText(String.valueOf(data.get(perHourTxt)));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
    private void moveCameratoLocation(LatLng location_lat_log) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(location_lat_log)
                .zoom(18).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}