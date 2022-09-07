package com.redxlab.kissan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    private static final String TAG ="SignIn";
    private EditText username,number,password;
    private Button login_button,createAccount_button;
    private String role,numberS,passwordS,currentUserNumber,userLocation;

    //    firebase Signup
    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data = new HashMap<>();
    private boolean passwordB=true,loginB=true;

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
        //        Firebase init
        firebaseApp= FirebaseApp.getInstance();
        db = FirebaseFirestore.getInstance();

        login_button.setOnLongClickListener(v->{
            SignInWithFirebase();
            if (loginB) startActivity(new Intent(getApplicationContext(),HomeScreen.class));
            return true;
        });


        login_button.setOnClickListener(v->{
            SignInWithFirebase();

            saveData();

        });

        createAccount_button.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),SignUp.class));
        });
    }

    private void SignInWithFirebase() {
        Log.d(TAG, "SignInWithFirebase: called");
        getLoginDetails();
//        id user exist signIn else signup
        CollectionReference Users = db.collection("Users");
        Users.whereEqualTo("contactNumber",numberS).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CheckPassword(document);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            Toast.makeText(getApplicationContext(),"User does not exist",Toast.LENGTH_SHORT).show();
                            loginB=false;
                        }
                        Log.d(TAG, "onComplete: login: "+loginB);
                    }
                });
    }



    private void getLoginDetails() {
        Log.d(TAG, "getLoginDetails: called");
        numberS=number.getText().toString();
        if (numberS.isEmpty()) numberS="7088212747";
        passwordS=password.getText().toString();
        if (passwordS.isEmpty()) passwordS="1234567890";
    }

    private boolean CheckPassword(QueryDocumentSnapshot document) {
        Log.d(TAG, "CheckPassword: called");
        data=document.getData();
        Log.d(TAG, "CheckPassword: data: "+data);
        String passwordTxt="password",locationTxt="village";
        userLocation=data.get(locationTxt).toString();
        String passFire=data.get(passwordTxt).toString();
        Log.d(TAG, "CheckPassword: password on firebase: "+passFire);
        if (passwordS.equals(passFire)) {
            saveData();
            if (role.equals("Farmer")) {
                startActivity(new Intent(getApplicationContext(),HomeScreen.class));
                Toast.makeText(getApplicationContext(),"Farmer",Toast.LENGTH_SHORT).show();
            }else{
                startActivity(new Intent(getApplicationContext(),Duty.class));
                Toast.makeText(getApplicationContext(),"SP",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Password incorrect",Toast.LENGTH_SHORT).show();
        }
        return passwordB;
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Role",role);
        editor.putString("village",userLocation);
        editor.putString("currentUserNumber",numberS);
        editor.apply();

    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        role=sharedPreferences.getString("Role","");
        Log.d(TAG, "loadData: Role : "+role);

    }
}