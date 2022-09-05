package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OTP extends AppCompatActivity {
    private static final String TAG ="OTP";
    private EditText otp;
    private Button submit;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp=findViewById(R.id.idOTP);
        submit=findViewById(R.id.idOTPSubmit);

        loadData();

        submit.setOnClickListener(v->{
            verifyOTP();
        });
    }

    private void verifyOTP() {
        if (role=="Farmer") {
            startActivity(new Intent(getApplicationContext(),HomeScreen.class));
        }else{
            startActivity(new Intent(getApplicationContext(),ServicesAvailable.class));
        }

    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        role=sharedPreferences.getString("Role","");
        Log.d(TAG, "loadData: Role "+role);

    }
}