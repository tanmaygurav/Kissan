package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AcceptedService extends AppCompatActivity {
    private TextView serviceName,requestedServiceName, serviceLocation, pricePerHour, pricePerHectare, coupon;
    private Button bookService,cancelService;
    private RelativeLayout acceptedServiceViews;
    private CardView requestedServiceCard;


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
        cancelService=findViewById(R.id.idCancelRequestBTN);

        acceptedServiceViews=findViewById(R.id.idAcceptedServiceViews);
        requestedServiceCard=findViewById(R.id.idRequestedServiceCard);

        acceptedServiceViews.setVisibility(View.GONE);

        Intent intent = getIntent();
        requestedServiceName.setText("Requested Service : "+intent.getStringExtra("Requested Service"));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestedServiceCard.setVisibility(View.GONE);
                acceptedServiceViews.setVisibility(View.VISIBLE);
            }
        }, 3000);

        bookService.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),BookedService.class));
        });

        cancelService.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),HomeScreen.class));
        });
    }
}