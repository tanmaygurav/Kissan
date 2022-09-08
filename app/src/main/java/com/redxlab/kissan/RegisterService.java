package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterService extends AppCompatActivity {
    private static final String TAG ="RegisterService";
    private EditText serviceName, pricePerHour, pricePerHectare;
    private Button addMore,save;
    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data= new HashMap<>();
    private String nameS="userName", mobileS="contactNumber", stateS="state", villageS="village"
            , passwordS="password",adhaarCardNumberS="adhaarNumber",pphour,pphectare;

    private String currentUserNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_service);

        serviceName=findViewById(R.id.idServiceName);
        pricePerHour=findViewById(R.id.idPricePerHour);
        pricePerHectare=findViewById(R.id.idPricePerHectare);

        addMore=findViewById(R.id.idAddServicesBTN);
        save=findViewById(R.id.idSaveBTN);

        //        Firebase init
        firebaseApp= FirebaseApp.getInstance();
        db = FirebaseFirestore.getInstance();

        loadData();

        save.setOnClickListener(v->{
            getServiceDetails();
            getSPDetails();

            startActivity(new Intent(getApplicationContext(),Duty.class));
        });
        addMore.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),ServicesAvailable.class));
        });
    }

    private void saveToFireBase() {
//        get service provider data and save request
        CollectionReference register = db.collection("AvailableService");
        data.put("serviceProviderName", nameS);
        data.put("serviceName", "Rotavator");
        data.put("pricePerHour", pphour);
        data.put("pricePerHectare", pphectare);
        data.put("contactNumber", currentUserNumber);
        data.put("location", "Meerut");
        data.put("accepted", "NO");
        register.add(data);
    }

    private void getServiceDetails() {
        pphectare=pricePerHectare.getText().toString();
        if (pphectare.isEmpty()) pphectare="2000";

        pphour=pricePerHour.getText().toString();
        if (pphour.isEmpty()) pphour="500";
    }

    private void getSPDetails() {
        CollectionReference Users = db.collection("Users");
        Users.whereEqualTo("contactNumber",currentUserNumber).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data=document.getData();
                                nameS=data.get(nameS).toString();
                                mobileS=data.get(mobileS).toString();
                                stateS=data.get(stateS).toString();
                                villageS=data.get(villageS).toString();
                                passwordS=data.get(passwordS).toString();
                                adhaarCardNumberS=data.get(adhaarCardNumberS).toString();
                                saveToFireBase();
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                });
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        currentUserNumber=sharedPreferences.getString("currentUserNumber","");
        Log.d(TAG, "loadData: currentUserNumber : "+currentUserNumber);

    }
}