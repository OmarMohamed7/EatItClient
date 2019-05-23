package com.example.rooot.eatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rooot.eatit.Interface.ItemClickListner;
import com.example.rooot.eatit.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_cart_name , txt_cart_price;
    public ImageView txt_cart_img;
    public RelativeLayout backgroundView;
    public LinearLayout foregroundView;

    private ItemClickListner itemClickListner;

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_cart_price = itemView.findViewById(R.id.cart_item_price);

        txt_cart_img = itemView.findViewById(R.id.cart_item_count);

        backgroundView = itemView.findViewById(R.id.view_background);
        foregroundView = itemView.findViewById(R.id.view_foreground);

    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view,getAdapterPosition() , false);
    }
}



