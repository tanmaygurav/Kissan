package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class AcceptedService extends AppCompatActivity {
    private static final String TAG ="AcceptedService";
    private TextView serviceName,requestedServiceName, pricePerHour, pricePerHectare, coupon;
    private EditText serviceLocation;
    private Button bookService,cancelService;
    private RelativeLayout acceptedServiceViews;
    private CardView requestedServiceCard;
    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Context context;
    private Map<String,Object> data;
    private String userLocation,farmerName;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_service);

        serviceName=findViewById(R.id.idServiceName);
        requestedServiceName=findViewById(R.id.idRequestedServiceName);
        serviceLocation=findViewById(R.id.idServiceLocation);
        pricePerHour=findViewById(R.id.idPricePerHour);
        pricePerHectare=findViewById(R.id.idPricePerHectare);
        coupon=findViewById(R.id.idCoupon);

        bookService=findViewById(R.id.idBookServiceBTN);


        acceptedServiceViews=findViewById(R.id.idAcceptedServiceViews);
        requestedServiceCard=findViewById(R.id.idRequestedServiceCard);

//        Firebase init
        firebaseApp= FirebaseApp.getInstance();
//        FirebaseApp.initializeApp(getApplicationContext());
        //        Firebase query to get requested service
        db = FirebaseFirestore.getInstance();


        intent = getIntent();
        serviceName.setText("Requested Service : "+intent.getStringExtra("Requested Service"));
        loadData();

//        get request price
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




        bookService.setOnClickListener(v->{
            sendRequestToFireBase();

        });


    }

    private void sendRequestToFireBase() {
        CollectionReference request = db.collection("RequestedService");
        data.put("accepted", "NO");
        data.put("farmerName", farmerName);
        request.document("request1").set(data);
        Intent intent1= new Intent(getApplicationContext(),BookedService.class);
        intent1.putExtra("Requested Service",intent.getStringExtra("Requested Service"));
        startActivity(intent1);
    }

    private void populateViews() {
        String serviceNameTxt = "serviceName",locationTxt="location",perHourTxt="pricePerHour",perHectareTxt="pricePerHectare";
        serviceName.setText(String.valueOf(data.get(serviceNameTxt)));
        serviceLocation.setText(userLocation);
        pricePerHour.setText(String.valueOf(data.get(perHourTxt)));
        pricePerHectare.setText(String.valueOf(data.get(perHectareTxt)));
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        userLocation=sharedPreferences.getString("village","");
        farmerName=sharedPreferences.getString("farmerName","");
        Log.d(TAG, "loadData: userLocation "+userLocation);

    }
}