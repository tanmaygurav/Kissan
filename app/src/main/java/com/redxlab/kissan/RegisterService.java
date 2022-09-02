package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class RegisterService extends AppCompatActivity {
    private EditText serviceName, pricePerHour, pricePerHectare;
    private Button addMore,save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_service);

        serviceName=findViewById(R.id.idServiceName);
        pricePerHour=findViewById(R.id.idPricePerHour);
        pricePerHectare=findViewById(R.id.idPricePerHectare);

        addMore=findViewById(R.id.idAddServicesBTN);
        save=findViewById(R.id.idSaveBTN);

        save.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),Duty.class));
        });
        addMore.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),ServicesAvailable.class));
        });
    }
}