package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JobNotification extends AppCompatActivity {
    private static final String TAG ="JobNotification";
    private TextView farmerName, requestedServiceName, requestedServiceLocation, pricePerHour;
    private Button accept, reject;
    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data= new HashMap<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_notification);

        farmerName=findViewById(R.id.idFarmerName);
        requestedServiceName=findViewById(R.id.idRequestedService);
        requestedServiceLocation=findViewById(R.id.idRequestedServiceLocation);
        pricePerHour=findViewById(R.id.idRequestedServicePriceHour);

        accept=findViewById(R.id.idAcceptJob);
        reject=findViewById(R.id.idRejectJob);

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

        reject.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),Duty.class));
        });

        accept.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),JobDetails.class));
        });
    }

    private void populateViews() {
        String serviceNameTxt = "serviceName",locationTxt="village",perHourTxt="pricePerHour",farmerNameTxt="farmerName";
        farmerName.setText(String.valueOf(data.get(farmerNameTxt)));
        requestedServiceName.setText("Service wanted "+String.valueOf(data.get(serviceNameTxt)));
        requestedServiceLocation.setText("Location "+String.valueOf(data.get(locationTxt)));
        pricePerHour.setText("Price per hour "+String.valueOf(data.get(perHourTxt)));
    }
}