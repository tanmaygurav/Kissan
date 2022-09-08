package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private Button cancelRequest,cancelService,callSP,share;

    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data;
    private String SPnumber;

    // monitor request
    private Handler handler = new Handler();
    private Runnable runnable;
    int delay = 10000;
    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable,delay);
                getSPFromFirebase();
            }
        },delay);
        super.onResume();
    }

    private void getSPFromFirebase() {
        db.collection("RequestedService").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                data=document.getData();
                                String acceptedtxt="accepted";
                                try {
                                    String accepted=data.get(acceptedtxt).toString();
                                    if (accepted.equals("YES")){
                                        requestedServiceCard.setVisibility(View.GONE);
                                        bookedServiceViews.setVisibility(View.VISIBLE);                                    };
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),"error: "+e,Toast.LENGTH_SHORT).show();
                                }}
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_service);

        bookedServiceViews=findViewById(R.id.idBookedServiceViews);
        requestedServiceCard=findViewById(R.id.idRequestedServiceCard);
        requestedServiceName=findViewById(R.id.idRequestedServiceName);
        cancelRequest=findViewById(R.id.idCancelRequestBTN);
        serviceProviderName=findViewById(R.id.idServiceProviderName);
        SPServiceName=findViewById(R.id.idServiceName);

        cancelService=findViewById(R.id.idCancelBTN);
        callSP=findViewById(R.id.idCallBTN);
        share=findViewById(R.id.idShareBTN);

        bookedServiceViews.setVisibility(View.GONE);

        Intent intent = getIntent();
        requestedServiceName.setText("Requested Service : "+intent.getStringExtra("Requested Service"));
//        firebase init
        firebaseApp= FirebaseApp.getInstance();
        db = FirebaseFirestore.getInstance();

//delayed service accepted code
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                requestedServiceCard.setVisibility(View.GONE);
//                bookedServiceViews.setVisibility(View.VISIBLE);
//            }
//        }, 3000);
//        TODO: monitor accepted flag | on true show details
//        monitor flag


        final DocumentReference docRef = db.collection("RequestedService").document("request1");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                if (value != null && value.exists()) {
                    Log.d(TAG, "Current data: " + value.getData());
                    String acceptedtxt="accepted";
                    String accepted=value.get(acceptedtxt).toString();
                    if (accepted.equals("true")){
                        requestedServiceCard.setVisibility(View.GONE);
                        bookedServiceViews.setVisibility(View.VISIBLE);
                    };
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
//        on true show details

        cancelRequest.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),HomeScreen.class));
        });
        getFromFirebase();

//        onclicks
        cancelService.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),HomeScreen.class));
        });

        callSP.setOnClickListener(v->{
            Intent call = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",SPnumber,null));
            startActivity(call);
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
        String contactNumber="contactNumber";
        SPServiceName.setText(String.valueOf(data.get(serviceNameTxt)));
        serviceProviderName.setText(String.valueOf(data.get(serviceProviderNameTxt)));
        SPnumber=data.get(contactNumber).toString();
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