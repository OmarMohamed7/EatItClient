package com.example.rooot.eatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.rooot.eatit.Interface.ItemClickListner;
import com.example.rooot.eatit.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId , txtOrderStatus , txtOrderPhone , txtOrderAddress , txtOrderTotalCash;
    private ItemClickListner itemClickListnerlistner;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderId = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
        txtOrderAddress = itemView.findViewById(R.id.order_address);
        txtOrderTotalCash = itemView.findViewById(R.id.order_total_cash);
        //itemView.setOnClickListener(this);

    }



    public void setOnItemClickListner(ItemClickListner itemClickListnerlistner){
        this.itemClickListnerlistner = itemClickListnerlistner;
    }

    @Override
    public void onClick(View view) {
        itemClickListnerlistner.onClick(view , getAdapterPosition(),false);
    }


}
