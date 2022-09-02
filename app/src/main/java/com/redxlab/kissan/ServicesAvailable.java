package com.redxlab.kissan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ServicesAvailable extends AppCompatActivity {
    private RecyclerView servicesAvailable;
    private ArrayList<ServicesModel> servicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_available);

        servicesAvailable=findViewById(R.id.idAvailableServicesRV);

        //        Available Services Recycler
        servicesList= new ArrayList<>();
        servicesList.add(new ServicesModel("Rotavator",R.drawable.rotavator_service));
        servicesList.add(new ServicesModel("Cultivator",R.drawable.cultivator_service));
        servicesList.add(new ServicesModel("Harrow",R.drawable.harrow_service));
        servicesList.add(new ServicesModel("Plough",R.drawable.plough_service));
        servicesList.add(new ServicesModel("Trailer",R.drawable.trailer_service));
        servicesList.add(new ServicesModel("Roto seed Drill",R.drawable.roto_seed_drill));
        servicesList.add(new ServicesModel("Baler",R.drawable.baler_service));
        servicesList.add(new ServicesModel("Planter",R.drawable.planter_service));
        servicesList.add(new ServicesModel("Sprayer",R.drawable.sprayer_service));
        servicesList.add(new ServicesModel("Straw Reaper",R.drawable.straw_reaper));
        servicesList.add(new ServicesModel("Digger",R.drawable.digger_service));
        servicesList.add(new ServicesModel("Thresher",R.drawable.thresher_service));

        ServicesAdapter servicesAdapter = new ServicesAdapter(this,servicesList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        servicesAvailable.setLayoutManager(gridLayoutManager);
        servicesAvailable.setAdapter(servicesAdapter);


    }
}