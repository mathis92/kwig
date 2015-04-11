package com.example.martinhudec.kwigBA.stopDetail;


import android.content.Context;
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
public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private LayoutInflater inflator;
    List<RouteDetail> data = Collections.emptyList();
    private Context context;

    public Adapter(Context context, List<RouteDetail> data) {
        Log.d("mathis", "CONSTURCTOR");
        inflator = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.stop_details_custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        Log.d("mathis", "onCreateViewHolder");

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RouteDetail routeDetail = data.get(position);
        Log.d("mathis", "on bind view holder " + position);
        holder.headingTo.setText(routeDetail.headingTo);
        holder.vehicleId.setText(routeDetail.vehicleId);
        holder.icon.setImageResource(routeDetail.vehicleTypeIcon);
        holder.arrivalTime.setText(routeDetail.arrivalTime);
        holder.delay.setText(routeDetail.delay);
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

        public MyViewHolder(View itemView) {

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
            Toast.makeText(context, "Item clicked at " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
