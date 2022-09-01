package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class OTP extends AppCompatActivity {
    private EditText otp;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp=findViewById(R.id.idOTP);
        submit=findViewById(R.id.idOTPSubmit);

        submit.setOnClickListener(v->{
            verifyOTP();
        });
    }

    private void verifyOTP() {
        startActivity(new Intent(getApplicationContext(),HomeScreen.class));
    }
}