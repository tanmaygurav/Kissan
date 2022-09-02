package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class Duty extends AppCompatActivity {
    private Button dutyStatusBTN,accountBTN;
    private boolean dutyStatus=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);

        dutyStatusBTN=findViewById(R.id.idDutyStatus);
        accountBTN=findViewById(R.id.idAccountBTN);

        dutyStatusBTN.setText("On Duty");

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
    }
}