package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
//    TODO: This is Sign Page
    private static final String TAG ="MainActivity";
    EditText username,email,password;
    Button login_button,createAccount_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        login_button=findViewById(R.id.login_button);
        createAccount_button=findViewById(R.id.createAccount_button);
        username=findViewById(R.id.user_name);

        login_button.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        });
    }
}