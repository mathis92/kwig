package com.example.martinhudec.kwigBA;


import android.content.Context;
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
    private Context context;

    public DrawerAdapter(Context context, List<Information> data) {
        Log.d("mathis", "CONSTURCTOR");
        inflator = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        Log.d("mathis", "onCreateViewHolder");

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        Log.d("mathis", "on bind view holder " + position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);

    }

    @Override
    public int getItemCount() {
        return 15;
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
            icon.setOnClickListener(this);
            Log.d("mathis", "MyViewHolder");
        }

        @Override
        public void onClick(View v) {
            delete(getAdapterPosition());
            Toast.makeText(context, "Item clicked at " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
