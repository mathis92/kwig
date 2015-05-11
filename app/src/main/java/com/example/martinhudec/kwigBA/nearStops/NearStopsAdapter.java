package com.example.martinhudec.kwigBA.nearStops;


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
import com.example.martinhudec.kwigBA.stopDetail.StopDetailsActivity;
import com.example.martinhudec.kwigBA.vehicleDetail.VehicleDetailsActivity;

import java.util.Collections;
import java.util.List;

/**
 * Created by martinhudec on 20/03/15.
 */
public class NearStopsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflator;
    List<StopDetailsWithRoutes> data = Collections.emptyList();
    private Activity activity;
    private RecyclerView recyclerView;
    int currentapiVersion = android.os.Build.VERSION.SDK_INT;


    public NearStopsAdapter(Activity activity, List<StopDetailsWithRoutes> data) {
        //   Log.d("mathis", "CONSTURCTOR");
        inflator = LayoutInflater.from(activity);
        this.data = data;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new StopDetailViewHolder(inflator.inflate(R.layout.near_stops_custom_row, parent, false));
                break;
            case 1:
                holder = new StopRoutesDetailViewHolder(inflator.inflate(R.layout.stop_details_custom_row, parent, false));
                break;
        }
        //  Log.d("mathis", "onCreateViewHolder");
        return holder;
    }

    @Override
    public int getItemViewType(int position) {

        if (position % 4 == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getStopDataPostition(int position) {
        Integer pos = null;
        if (position < 4) {
            pos = 0;
        } else if (position < 8) {
            pos = 1;
        } else if (position < 12) {
            pos = 2;
        }

        return pos;
    }

    public int getRouteDataPosition(int position) {
        return (position % 4) - 1;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        // Log.d("mathis", "on bind view holder " + position + " datasize " + data.size() );

        switch (getItemViewType(position)) {
            case 0:
                ((StopDetailViewHolder) holder).stopName.setText(data.get(getStopDataPostition(position)).stop.getStopName());
                holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.stop));
                if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.itemView.setElevation(17);
                }

                break;
            case 1:
                if (data.get(getStopDataPostition(position)).routeDetailList.size() >= getRouteDataPosition(position) + 1) {
                    ((StopRoutesDetailViewHolder) holder).headingTo.setText(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getHeadingTo());
                    ((StopRoutesDetailViewHolder) holder).arrivalTime.setText(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getArrivalTime());
                    ((StopRoutesDetailViewHolder) holder).delay.setText(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getDelayHMS());
                    ((StopRoutesDetailViewHolder) holder).vehicleId.setText(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getVehicleShortName());
                    switch (data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getVehicleType()){
                        case 0: ((StopRoutesDetailViewHolder) holder).icon.setColorFilter(activity.getResources().getColor(R.color.tram));
                            break;
                        case 2: ((StopRoutesDetailViewHolder) holder).icon.setColorFilter(activity.getResources().getColor(R.color.trolleybus));
                            break;
                        case 3: ((StopRoutesDetailViewHolder) holder).icon.setColorFilter(activity.getResources().getColor(R.color.bus));
                            break;
                    }
                    ((StopRoutesDetailViewHolder) holder).icon.setImageResource(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getVehicleTypeIcon());

                    if (!data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getDelay().equals("notStarted")) {
                        if (Delay.getDelayLength(Integer.parseInt(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getDelay())) > 0) {
                            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.vehicleDelay));
                            if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                                holder.itemView.setElevation(10);
                            }
                        } else {
                            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.vehicleOnTime));
                        }
                    } else {
                        holder.itemView.setBackgroundColor(Color.WHITE);
                        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
                            holder.itemView.setElevation(2);
                        }
                    }


                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 12;
    }


    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    class StopRoutesDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView headingTo;
        TextView vehicleId;
        TextView arrivalTime;
        ImageView icon;
        TextView delay;
        View itemView;

        public StopRoutesDetailViewHolder(View itemView) {

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
            // Toast.makeText(activity, "VEHICLE DATA " + getPosition(), Toast.LENGTH_SHORT).show();

            Intent vehicleDetailsActivity = new Intent(activity, VehicleDetailsActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("vehicle", data.get(getStopDataPostition(getPosition())).routeDetailList.get(getRouteDataPosition(getPosition()))); //Your id

            vehicleDetailsActivity.putExtras(b); //Put your id to your next Intent
            activity.startActivity(vehicleDetailsActivity);
            activity.overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);

        }
    }

    class StopDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView stopName;


        public StopDetailViewHolder(View itemView) {

            super(itemView);

            stopName = (TextView) itemView.findViewById(R.id.near_stops_custom_row_stop_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //    Toast.makeText(activity, "STOP DATA", Toast.LENGTH_SHORT).show();
            Intent stopDetailsActivity = new Intent(activity, StopDetailsActivity.class);
            Bundle b = new Bundle();
            b.putString("stopName", data.get(getStopDataPostition(getPosition())).stop.getStopName()); //Your id
            stopDetailsActivity.putExtras(b); //Put your id to your next Intent
            activity.startActivity(stopDetailsActivity);
            activity.overridePendingTransition(R.anim.activity_animation, R.anim.activity_animation2);

        }
    }

}
