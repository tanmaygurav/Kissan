package com.redxlab.kissan;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ServicesModel> servicesModelArrayList;


    public ServicesAdapter(Context context, ArrayList<ServicesModel> servicesModelArrayList) {
        this.context = context;
        this.servicesModelArrayList = servicesModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServicesModel model = servicesModelArrayList.get(position);
        holder.thumbnail.setImageResource(model.getServiceImage());
        holder.title.setText(model.getServiceName());

        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs",Context.MODE_PRIVATE);
        String role=sharedPreferences.getString("Role","");
        Log.d("Services Adapter", "loadData: Role "+role);

        if (role.equals("Farmer")) {
            holder.itemView.setOnClickListener(view -> {
                Toast.makeText(context,holder.title.getText().toString()+" Clicked",Toast.LENGTH_SHORT).show();
                Intent viewIntent = new Intent(context,AcceptedService.class);
                viewIntent.putExtra("Requested Service",holder.title.getText().toString());
                context.startActivity(viewIntent);
            });
        }else{
            holder.itemView.setOnClickListener(view -> {
                Toast.makeText(context,holder.title.getText().toString()+" Clicked",Toast.LENGTH_SHORT).show();
                Intent viewIntent = new Intent(context,RegisterService.class);
                viewIntent.putExtra("Requested Service",holder.title.getText().toString());
                context.startActivity(viewIntent);
            });
        }


    }

    private void loadData() {

    }

    @Override
    public int getItemCount() {
        return servicesModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView thumbnail;
        private TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail=itemView.findViewById(R.id.idThumbnail);
            title=itemView.findViewById(R.id.idTitle);

        }
    }

}
