package com.example.martinhudec.kwigBA.findStop;


import android.app.Activity;
import android.app.SearchManager;
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

import com.example.martinhudec.kwigBA.R;
import com.example.martinhudec.kwigBA.stopDetail.RouteDetail;

import java.util.Collections;
import java.util.List;

/**
 * Created by martinhudec on 20/03/15.
 */
public class SuggestionsAdapter extends RecyclerView.Adapter<SuggestionsAdapter.MyViewHolder> {
    private LayoutInflater inflator;
    List<String> data = Collections.emptyList();
    private Activity activity;



    public SuggestionsAdapter(Activity activity, List<String> data) {
        Log.d("mathis", "CONSTURCTOR");
        inflator = LayoutInflater.from(activity);
        this.data = data;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.suggested_stop_custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        Log.d("mathis", "onCreateViewHolder");

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.d("mathis", "on bind view holder " + position);
        holder.stopName.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView stopName;


        public MyViewHolder(View itemView) {

            super(itemView);
            stopName = (TextView) itemView.findViewById(R.id.stop_custom_row_stop_name);
            stopName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
         //   Toast.makeText(activity, "Item clicked at " + getPosition(), Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent();
            //intent.setAction(Intent.ACTION_SEARCH);
            //intent.putExtra("string", SearchManager.QUERY);
           // new FindStopActivity().getStopContent(data.get(getPosition()));
            ((FindStopActivity)activity).getStopContent(data.get(getPosition()));
        }
    }
}
