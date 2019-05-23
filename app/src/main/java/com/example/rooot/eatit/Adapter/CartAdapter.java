package com.example.rooot.eatit.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.rooot.eatit.Model.Order;
import com.example.rooot.eatit.R;
import com.example.rooot.eatit.ViewHolder.CartViewHolder;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData;
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CartViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.cart_layout , viewGroup , false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable textDrawable = TextDrawable.builder()
                .buildRound(""+listData.get(position).getQuantity() , Color.RED);

        holder.txt_cart_name.setText(listData.get(position).getProductName());
        holder.txt_cart_img.setImageDrawable(textDrawable);


        Locale local = new Locale("en" , "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(local);

        //System.out.println(listData.get(position).getPrice());

        int price = (Integer.valueOf(listData.get(position).getPrice()))
                * (Integer.valueOf(listData.get(position).getQuantity()));

        Log.i("Price : ",String.valueOf(price));

        holder.txt_cart_price.setText(fmt.format(price));


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public Order getItem(int position){
        return listData.get(position);
    }

    public void removeItem(int position){
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item , int position){
        listData.add(position , item);
        notifyItemInserted(position);
    }

}