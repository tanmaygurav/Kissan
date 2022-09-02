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

public class SignIn extends AppCompatActivity {
    private static final String TAG ="SignIn";
    private EditText username,number,password;
    private Button login_button,createAccount_button;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        number=findViewById(R.id.idMobileNumber);
        password=findViewById(R.id.idPassword);
        login_button=findViewById(R.id.login_button);
        createAccount_button=findViewById(R.id.createAccount_button);
//        username=findViewById(R.id.user_name);

        loadData();
        login_button.setOnClickListener(v->{
            if (role=="Farmer") {
                startActivity(new Intent(getApplicationContext(),HomeScreen.class));
            }else{
                startActivity(new Intent(getApplicationContext(),ServicesAvailable.class));
            }

        });

        createAccount_button.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),SignUp.class));
        });
    }
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        role=sharedPreferences.getString("Role","");
        Log.d(TAG, "loadData: Role "+role);

    }
}