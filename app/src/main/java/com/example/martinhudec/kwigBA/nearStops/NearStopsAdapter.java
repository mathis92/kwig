package com.example.martinhudec.kwigBA.nearStops;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinhudec.kwigBA.R;

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


    public NearStopsAdapter(Activity activity, List<StopDetailsWithRoutes> data) {
        Log.d("mathis", "CONSTURCTOR");
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
        Log.d("mathis", "onCreateViewHolder");
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
        if(position <4){
            pos = 0;
        }else if (position < 8){
            pos = 1;
        }else if(position < 12){
            pos = 2;
        }

        return pos;
    }

    public int getRouteDataPosition(int position){
        return (position % 4) - 1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Log.d("mathis", "on bind view holder " + position + " datasize " + data.size() );

        switch (getItemViewType(position)) {
            case 0:
                ((StopDetailViewHolder) holder).stopName.setText(data.get(getStopDataPostition(position)).stop.getStopName());

                break;
            case 1:
                if (data.get(getStopDataPostition(position)).routeDetailList.size() >= getRouteDataPosition(position) +1) {
                    ((StopRoutesDetailViewHolder) holder).headingTo.setText(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getHeadingTo());
                    ((StopRoutesDetailViewHolder) holder).arrivalTime.setText(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getArrivalTime());
                    ((StopRoutesDetailViewHolder) holder).delay.setText(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getDelay());
                    ((StopRoutesDetailViewHolder) holder).vehicleId.setText(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getVehicleId());
                    ((StopRoutesDetailViewHolder) holder).icon.setImageResource(data.get(getStopDataPostition(position)).routeDetailList.get(getRouteDataPosition(position)).getVehicleTypeIcon());
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

        public StopRoutesDetailViewHolder(View itemView) {

            super(itemView);
            headingTo = (TextView) itemView.findViewById(R.id.stop_custom_row_route_heading_to);
            vehicleId = (TextView) itemView.findViewById(R.id.stop_custom_row_route_id);
            icon = (ImageView) itemView.findViewById(R.id.stop_custom_row_route_icon);
            arrivalTime = (TextView) itemView.findViewById(R.id.stop_custom_row_route_arrival_time);
            delay = (TextView) itemView.findViewById(R.id.stop_custom_row_route_delay);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(activity, "Item clicked at " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    class StopDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView stopName;


        public StopDetailViewHolder(View itemView) {

            super(itemView);

            stopName = (TextView) itemView.findViewById(R.id.near_stops_custom_row_stop_name);

        }

        @Override
        public void onClick(View v) {
            //   Toast.makeText(activity, "Item clicked at " + getPosition(), Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent();
            //intent.setAction(Intent.ACTION_SEARCH);
            //intent.putExtra("string", SearchManager.QUERY);
            // new FindStopActivity().getStopContent(data.get(getPosition()));
            //((FindStopActivity)activity).getStopContent(data.get(getPosition()).getStopName());
        }
    }

}
