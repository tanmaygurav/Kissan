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
import android.widget.TextView;

public class SignUp extends AppCompatActivity {
    private static final String TAG ="SignUp";
    private TextView registerHeader,adhaarCardTV;
    private EditText name, mobile, state, village, password, confirmPassword,adhaarCardNumber;
    private Button registerBTN;
    private String nameS, mobileS, stateS, villageS, passwordS,role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        registerHeader=findViewById(R.id.idRegisterHeader);

        name=findViewById(R.id.idUserNameET);
        mobile=findViewById(R.id.idMobileNumber);
        state=findViewById(R.id.idState);
        village=findViewById(R.id.idVillage);
        password=findViewById(R.id.idPassword);
        confirmPassword=findViewById(R.id.idConPassword);
        adhaarCardTV=findViewById(R.id.idAdhaarCardHeader);
        adhaarCardNumber=findViewById(R.id.idAdhaarCardNumber);

        registerBTN=findViewById(R.id.idRegisterSubmit);

        loadData();
        
        registerBTN.setOnClickListener(v->{
            saveData();
            startActivity(new Intent(getApplicationContext(),OTP.class));
        });

        if (role=="Farmer") {
            registerHeader.setText("Register as Farmer");
            adhaarCardNumber.setVisibility(View.GONE);
            adhaarCardTV.setVisibility(View.GONE);
        }else{
            registerHeader.setText("Register as Service Provider");
        }
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        role=sharedPreferences.getString("Role","");
        Log.d(TAG, "loadData: Role "+role);

    }

    private void saveData() {
        getRegisterDetails();
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("UserName",nameS);
        editor.putString("mobile",mobileS);
        editor.putString("state",stateS);
        editor.putString("village",villageS);
        editor.putString("password",passwordS);

        editor.apply();

        Log.d(TAG, "saveData: UserName : "+nameS);
        Log.d(TAG, "saveData: mobile : "+mobileS);
        Log.d(TAG, "saveData: state : "+stateS);
        Log.d(TAG, "saveData: village : "+villageS);
        Log.d(TAG, "saveData: password : "+passwordS);


    }

    private void getRegisterDetails() {
        nameS=name.getText().toString();
        if (nameS.isEmpty()) nameS="Prince Chaudhary";

        mobileS=mobile.getText().toString();
        if (mobileS.isEmpty()) mobileS="7088212747";

        stateS=state.getText().toString();
        if (stateS.isEmpty()) stateS="Maharashtra";

        villageS=village.getText().toString();
        if (villageS.isEmpty()) villageS="Nagpur";

        passwordS=password.getText().toString();
        if (passwordS.isEmpty()) passwordS="1234567890";
    }
}