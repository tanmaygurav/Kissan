package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Duty extends AppCompatActivity {
    private Button dutyStatusBTN,accountBTN,addServices;
    private boolean dutyStatus=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);

        dutyStatusBTN=findViewById(R.id.idDutyStatus);
        accountBTN=findViewById(R.id.idAccountBTN);
        addServices=findViewById(R.id.idAddServicesBTN);

        dutyStatusBTN.setText("On Duty");

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
}