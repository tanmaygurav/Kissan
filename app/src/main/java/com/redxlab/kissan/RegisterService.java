package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterService extends AppCompatActivity {
    private EditText serviceName, pricePerHour, pricePerHectare;
    private Button addMore,save;
    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data= new HashMap<>();

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

        save.setOnClickListener(v->{
            saveToFireBase();
            startActivity(new Intent(getApplicationContext(),Duty.class));
        });
        addMore.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),ServicesAvailable.class));
        });
    }

    private void saveToFireBase() {
        CollectionReference request = db.collection("RequestedService");
        docRef = db.collection("RequestedService").document("request1");
        data.put("serviceProviderName", "Prince");
        data.put("serviceName", "Rotavator");
        data.put("pricePerHour", 500);
        data.put("pricePerHectare", 1000);
        data.put("contactNumber", "7088212747");
        data.put("location", "Meerut");
        request.document("request1").set(data);
    }
}