package com.example.flowerserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.flowerserver.Common.Common;
import com.example.flowerserver.Interface.ItemClickListener;
import com.example.flowerserver.R;


public class FlowerViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener {

    public TextView flower_name;
    public ImageView flower_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FlowerViewHolder(@NonNull View itemView) {
        super(itemView);

        flower_name = (TextView)itemView.findViewById(R.id.flower_name);
        flower_image = (ImageView)itemView.findViewById(R.id.flower_image);

        itemView.setOnCreateContextMenuListener(this);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Chọn hành động");

        menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
        menu.add(0, 0, getAdapterPosition(), Common.DELETE);
    }
}