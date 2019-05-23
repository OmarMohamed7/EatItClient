package com.example.rooot.eatit.ViewHolder;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.rooot.eatit.Interface.ItemClickListner;
import com.example.rooot.eatit.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    Typeface face;
    public TextView txtMenuName , txtMenuHint , txtMenuPrice;
    public ImageView imgMenu;
    private ItemClickListner itemClickListner;
    public FloatingActionButton addToCart;
    public ElegantNumberButton quantityNumberButton;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName = itemView.findViewById(R.id.txtMenuName);
        txtMenuHint = itemView.findViewById(R.id.txtMenuHint);
        txtMenuPrice = itemView.findViewById(R.id.txtMenuPrice);
        addToCart = itemView.findViewById(R.id.add_to_cart);
        quantityNumberButton = itemView.findViewById(R.id.counter);
        quantityNumberButton.setNumber("1");
        quantityNumberButton.setRange(1,10);
        imgMenu = itemView.findViewById(R.id.imgMenu);

//        face = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Insomnia.ttf");
//        txtMenuName.setTypeface(face);

        //itemView.setOnClickListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view , getAdapterPosition(),false);
    }


}
