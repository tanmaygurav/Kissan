package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class RoleSelector extends AppCompatActivity {
    private static final String TAG ="RoleSelector";
    private CardView serviceProvider, farmer;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selector);

        serviceProvider=findViewById(R.id.idServiceProvider);
        farmer=findViewById(R.id.idFarmer);

        serviceProvider.setOnClickListener(v->{
            role="Service Provider";
            saveData();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        });

        farmer.setOnClickListener(v->{
            role="Farmer";
            saveData();
            startActivity(new Intent(getApplicationContext(),SignIn.class));
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("UserName",role);

        editor.apply();
        Log.d(TAG, "saveData: Role : "+role);
    }
}