package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class Duty extends AppCompatActivity {
    private static final String TAG ="Duty";
    private Button dutyStatusBTN,accountBTN,addServices;
    private boolean dutyStatus=true;
    private String currentUserNumber;

    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data;
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
                getRequestsFromFirebase();
            }
        },delay);
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }

    private void getRequestsFromFirebase() {
        //        monitor  request flag
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
                                    if (!accepted.isEmpty()){
                                        startActivity(new Intent(getApplicationContext(),JobNotification.class));
                                        finish();
                                    };
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