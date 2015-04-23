package com.example.martinhudec.kwigBA.vehicleDetail;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.equip.Delay;
import com.example.martinhudec.kwigBA.map.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinhudec on 20/03/15.
 */
public class VehicleDetailsAdapter extends RecyclerView.Adapter<VehicleDetailsAdapter.MyViewHolder> {
    private LayoutInflater inflator;
    Vehicle vehicle;
    List<VehicleDetail> vehicleDetailList;
    private Context context;

    public VehicleDetailsAdapter(Context context, Vehicle vehicle) {
       // Log.d("mathis", "CONSTURCTOR");
        inflator = LayoutInflater.from(context);
        this.vehicle = vehicle;
        this.context = context;
        fillVehicleDetailList();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.vehicle_detail_card_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        //Log.d("mathis", "onCreateViewHolder");

        return holder;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VehicleDetail vehicleDetail = vehicleDetailList.get(position);
        //Log.d("mathis", "on bind view holder " + position);
        holder.icon.setImageResource(vehicleDetail.icon);
        holder.text.setText(vehicleDetail.text);
        holder.textData.setText(vehicleDetail.textData);
//        Log.d("TEXTDATA", vehicleDetail.textData);
        if(position == 3){
            if(!vehicleDetail.textData.equals("notStarted")) {
                if(vehicleDetail.textData.equals("on time") || vehicleDetail.textData.contains("ahead") || vehicleDetail.textData.equals("na čas") || vehicleDetail.textData.contains("v predstihu")){
                    holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.vehicleOnTime));
                } else {
                    holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.vehicleDelay));
                    holder.itemView.setElevation(10);
                }
            }else {
                holder.itemView.setBackgroundColor(Color.WHITE);
                 holder.itemView.setElevation(2);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (vehicleDetailList.size() > 15) {
            return 15;
        }

        return vehicleDetailList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView text;
        TextView textData;
        CardView cardView;


        public MyViewHolder(View itemView) {

            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            icon = (ImageView) itemView.findViewById(R.id.vehicle_detail_image);
            icon.setColorFilter(context.getResources().getColor(R.color.background_material_dark));
            text = (TextView) itemView.findViewById(R.id.vehicle_detail_text);
            textData = (TextView)  itemView.findViewById(R.id.vehicle_detail_text_data);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Item clicked at " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
    public void fillVehicleDetailList(){
        vehicleDetailList = new ArrayList<>();
        String[] vehicleDetailTexts = context.getResources().getStringArray(R.array.vehicle_detail);

        vehicleDetailList.add(new VehicleDetail(R.drawable.next_stop,vehicleDetailTexts[1], vehicle.getNextStop()));
        vehicleDetailList.add(new VehicleDetail(R.drawable.heading_to,vehicleDetailTexts[2], vehicle.getHeadingTo()));
        vehicleDetailList.add(new VehicleDetail(R.drawable.delay,vehicleDetailTexts[3], vehicle.getArrivalTime()));
        vehicleDetailList.add(new VehicleDetail(R.drawable.delay, vehicleDetailTexts[4], vehicle.getDelayHMS()));
        vehicleDetailList.add(new VehicleDetail(R.drawable.next_stop, vehicleDetailTexts[5], vehicle.getLastStop()));
        vehicleDetailList.add(new VehicleDetail(R.drawable.info, vehicleDetailTexts[6], vehicle.getSpeed()));
    }
    private class VehicleDetail{
        int icon;
        String text;
        String textData;

        public VehicleDetail(int icon, String text, String textData) {
            this.icon = icon;
            this.text = text;
            this.textData = textData;
        }

        public VehicleDetail(String text, String textData) {
            this.text = text;
            this.textData = textData;
        }
    }
}
