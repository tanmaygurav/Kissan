package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignIn extends AppCompatActivity {
    private static final String TAG ="SignIn";
    private EditText username,number,password;
    private Button login_button,createAccount_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        number=findViewById(R.id.idMobileNumber);
        password=findViewById(R.id.idPassword);
        login_button=findViewById(R.id.login_button);
        createAccount_button=findViewById(R.id.createAccount_button);
//        username=findViewById(R.id.user_name);

        login_button.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),HomeScreen.class));
        });

        createAccount_button.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),SignUp.class));
        });
    }
}