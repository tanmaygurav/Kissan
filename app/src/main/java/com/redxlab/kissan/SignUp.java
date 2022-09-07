package com.redxlab.kissan;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private static final String TAG ="SignUp";
    private TextView registerHeader,adhaarCardTV;
    private EditText name, mobile, state, village, password, confirmPassword,adhaarCardNumber;
    private Button registerBTN;
    private String nameS, mobileS, stateS, villageS, passwordS,adhaarCardNumberS,role;

//    firebase Signup
    private FirebaseApp firebaseApp;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    private Map<String,Object> data = new HashMap<>();;
    boolean EXIST_USER=true,SignUp_S=true;

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
        //        Firebase init
        firebaseApp= FirebaseApp.getInstance();
        db = FirebaseFirestore.getInstance();
        
        registerBTN.setOnClickListener(v->{
            saveData();
            if (role.equals("Farmer")) SignUpWithFirebaseFarmer();
            else SignUpWithFirebaseSP();
        });

        if (role.equals("Farmer")) {
            registerHeader.setText("Register as Farmer");
            adhaarCardNumber.setVisibility(View.GONE);
            adhaarCardTV.setVisibility(View.GONE);
        }else{
            registerHeader.setText("Register as Service Provider");
        }
    }

    private void SignUpWithFirebaseSP() {
        Log.d(TAG, "SignUpWithFirebaseSP: called");
        //        sign up if user does not exist else sign in
        if (CheckIfUserExists()){
            CollectionReference Users = db.collection("Users");
            data.put("userType", role);
            data.put("userName", nameS);
            data.put("contactNumber", mobileS);
            data.put("state",stateS);
            data.put("village", villageS);
            data.put("password", passwordS);
            data.put("adhaarNumber", adhaarCardNumberS);
            Users.document(mobileS).set(data);
            Toast.makeText(getApplicationContext(),"User Sign In : "+nameS,Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(),ServicesAvailable.class));
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
        editor.putString("adhaarNumber",adhaarCardNumberS);
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

        adhaarCardNumberS=adhaarCardNumber.getText().toString();
        if (adhaarCardNumberS.isEmpty()) adhaarCardNumberS="123456789012";
    }

    public void SignUpWithFirebaseFarmer(){
        Log.d(TAG, "SignUpWithFirebaseFarmer: called");
//        sign up if user does not exist else sign in
        if (CheckIfUserExists()){
            CollectionReference Users = db.collection("Users");
            data.put("userType", role);
            data.put("userName", nameS);
            data.put("contactNumber", mobileS);
            data.put("state",stateS);
            data.put("village", villageS);
            data.put("password", passwordS);
            saveFarmerName();
            Users.document(mobileS).set(data);
            startActivity(new Intent(getApplicationContext(),HomeScreen.class));
        }
    }

    private void saveFarmerName() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("farmerName",nameS);
    }

    private boolean CheckIfUserExists() {
        Log.d(TAG, "CheckIfUserExists: called");
        CollectionReference Users = db.collection("Users");
        Users.whereEqualTo("contactNumber",mobileS).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Toast.makeText(getApplicationContext(),"User already exists",Toast.LENGTH_SHORT).show();
                                EXIST_USER=true;
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            EXIST_USER=false;
                        }
                    }
                });
        Log.d(TAG, "CheckIfUserExists: "+EXIST_USER);
        return EXIST_USER;
    }

    ;
}