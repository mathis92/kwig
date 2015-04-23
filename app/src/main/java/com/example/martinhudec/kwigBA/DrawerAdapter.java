package com.example.martinhudec.kwigBA;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by martinhudec on 20/03/15.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {
    private LayoutInflater inflator;
    List<Information> data = Collections.emptyList();
    private Activity activity;

    public DrawerAdapter(Activity activity, List<Information> data) {
    //    Log.d("mathis", "CONSTURCTOR");
        inflator = LayoutInflater.from(activity);
        this.data = data;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
      //  Log.d("mathis", "onCreateViewHolder");

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
       // Log.d("mathis", "on bind view holder " + position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {

            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listText);
            icon = (ImageView) itemView.findViewById(R.id.listIcon);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(getPosition() == 0){
                if(getPosition() == 0){
                    Intent settingsActivity = new Intent(activity, SettingsActivity.class);
                    activity.startActivity(settingsActivity);
                    activity.overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);
                }
            }
            Toast.makeText(activity, "Item clicked at " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
