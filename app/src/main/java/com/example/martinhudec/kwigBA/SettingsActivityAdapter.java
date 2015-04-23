package com.example.martinhudec.kwigBA;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinhudec.kwigBA.map.Vehicle;
import com.example.martinhudec.kwigBA.vehicleDetail.VehicleDetailsActivity;
import com.gc.materialdesign.views.ButtonFlat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinhudec on 20/03/15.
 */
public class SettingsActivityAdapter extends RecyclerView.Adapter<SettingsActivityAdapter.MyViewHolder> {
    private LayoutInflater inflator;
    private Activity activity;

    public SettingsActivityAdapter(Activity activity, String str) {
       // Log.d("mathis", "CONSTURCTOR");
        inflator = LayoutInflater.from(activity);
        this.activity = activity;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.settings_language_custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        //Log.d("mathis", "onCreateViewHolder");

        return holder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == 1) {
            holder.slovak.setSelected(true);
            holder.slovak.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ButtonFlat slovak;
        ButtonFlat english;

        public MyViewHolder(View itemView) {

            super(itemView);
            slovak = (ButtonFlat) itemView.findViewById(R.id.buttonflatSlovak);
            english = (ButtonFlat) itemView.findViewById(R.id.buttonflatEnglish);
            english.setOnClickListener(this);
            slovak.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(activity,((Integer)v.getId()).toString(),Toast.LENGTH_SHORT).show();


         //   Toast.makeText(context, "Item clicked at " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }


}
