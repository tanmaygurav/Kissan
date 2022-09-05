package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class JobDetails extends AppCompatActivity {
    private static final String TAG ="JobNotification";
    private TextView farmerName,contactNumber,jobLocation,serviceName,pricePerHour;
    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data;
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
    }

    private void populateViews() {
        String serviceNameTxt = "serviceName",locationTxt="location";
        String perHourTxt="pricePerHour",farmerNameTxt="farmerName";
        String contactNumberTxt="contactNumber";
        farmerName.setText(String.valueOf(data.get(farmerNameTxt)));
        contactNumber.setText(String.valueOf(data.get(contactNumberTxt)));
        serviceName.setText(String.valueOf(data.get(serviceNameTxt)));
        jobLocation.setText(String.valueOf(data.get(locationTxt)));
        pricePerHour.setText(String.valueOf(data.get(perHourTxt)));
    }
}