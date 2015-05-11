package com.example.martinhudec.kwigBA.stopDetail;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.equip.Delay;
import com.example.martinhudec.kwigBA.vehicleDetail.VehicleDetailsActivity;

import java.util.Collections;
import java.util.List;

/**
 * Created by martinhudec on 20/03/15.
 */
public class StopDetailsAdapter extends RecyclerView.Adapter<StopDetailsAdapter.MyViewHolder> {
    private LayoutInflater inflator;
    List<RouteDetail> data = Collections.emptyList();
    private Activity activity;
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;

    public StopDetailsAdapter(Activity activity, List<RouteDetail> data) {
      //  Log.d("mathis", "CONSTURCTOR");
        inflator = LayoutInflater.from(activity);
        this.data = data;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.stop_details_custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
   //     Log.d("mathis", "onCreateViewHolder");

        return holder;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RouteDetail routeDetail = data.get(position);
  //      Log.d("mathis", "on bind view holder " + position);
        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        holder.headingTo.setText(routeDetail.headingTo);
        holder.vehicleId.setText(routeDetail.vehicleShortName);

        switch (routeDetail.getVehicleType()){
            case 0: holder.icon.setColorFilter(activity.getResources().getColor(R.color.tram));
                break;
            case 2: holder.icon.setColorFilter(activity.getResources().getColor(R.color.trolleybus));
                break;
            case 3: holder.icon.setColorFilter(activity.getResources().getColor(R.color.bus));
                break;
        }
        holder.icon.setImageResource(routeDetail.vehicleTypeIcon);

        holder.arrivalTime.setText(routeDetail.arrivalTime);
        holder.delay.setText(routeDetail.getDelayHMS());
     //   Log.d("stopDETAILS ADAPTER",routeDetail.getDelay());
        if(!routeDetail.getDelay().equals("notStarted")) {
            if (Delay.getDelayLength(Integer.parseInt(routeDetail.getDelay())) > 0) {
                holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.vehicleDelay));
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.itemView.setElevation(10);
                }
            } else {
                holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.vehicleOnTime));
            }
        }else {
            holder.itemView.setBackgroundColor(Color.WHITE);
            if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                holder.itemView.setElevation(2);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (data.size() > 15) {
            return 15;
        }

        return data.size();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView headingTo;
        TextView vehicleId;
        TextView arrivalTime;
        ImageView icon;
        TextView delay;
        View itemView;


        public MyViewHolder(View itemView) {

            super(itemView);
            headingTo = (TextView) itemView.findViewById(R.id.stop_custom_row_route_heading_to);
            vehicleId = (TextView) itemView.findViewById(R.id.stop_custom_row_route_short_name);
            icon = (ImageView) itemView.findViewById(R.id.stop_custom_row_route_icon);
            arrivalTime = (TextView) itemView.findViewById(R.id.stop_custom_row_route_arrival_time);
            delay = (TextView) itemView.findViewById(R.id.stop_custom_row_route_delay);
            this.itemView = itemView;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context, "Item clicked at " + getPosition(), Toast.LENGTH_SHORT).show();
            Intent vehicleDetailsActivity = new Intent(activity, VehicleDetailsActivity.class);
            Bundle b = new Bundle();
        //    Log.d("ROUTE DETAIL", data.get(getPosition()).getVehicleId());
            b.putSerializable("vehicle", data.get(getPosition())); //Your id

            vehicleDetailsActivity.putExtras(b); //Put your id to your next Intent
            activity.startActivity(vehicleDetailsActivity);
            activity.overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);
        }
    }
}
