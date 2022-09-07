package com.redxlab.kissan;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class Duty extends AppCompatActivity {
    private static final String TAG ="Duty";
    private Button dutyStatusBTN,accountBTN,addServices;
    private boolean dutyStatus=true;
    private String currentUserNumber;

    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);

        dutyStatusBTN=findViewById(R.id.idDutyStatus);
        accountBTN=findViewById(R.id.idAccountBTN);
        addServices=findViewById(R.id.idAddServicesBTN);

        dutyStatusBTN.setText("On Duty");

        loadData();

        //        firebase init
        firebaseApp= FirebaseApp.getInstance();
        db = FirebaseFirestore.getInstance();

        //        monitor  request flag
        docRef = db.collection("RequestedService").document("request1");
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
                    try {
                        String accepted=value.get(acceptedtxt).toString();
                        if (accepted.equals("YES")){
                            startActivity(new Intent(getApplicationContext(),JobNotification.class));
                        };
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"error: "+e,Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        addServices.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),ServicesAvailable.class));
        });

        dutyStatusBTN.setOnClickListener(v->{
            if (dutyStatus){
                dutyStatusBTN.setText("Off Duty");
                dutyStatusBTN.setBackgroundColor(getResources().getColor(R.color.red));
                dutyStatus=false;
            }else{
                dutyStatusBTN.setText("On Duty");
                dutyStatusBTN.setBackgroundColor(getResources().getColor(R.color.secondary));
                dutyStatus=true;
            }

        });

        dutyStatusBTN.setOnLongClickListener(v->{
            startActivity(new Intent(getApplicationContext(),JobNotification.class));
            return true;
        });
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        currentUserNumber=sharedPreferences.getString("currentUserNumber","");
        Log.d(TAG, "loadData: currentUserNumber : "+currentUserNumber);

    }
}