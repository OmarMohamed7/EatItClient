package com.example.rooot.eatit.Adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rooot.eatit.Common.Common;
import com.example.rooot.eatit.Interface.ItemClickListner;
import com.example.rooot.eatit.Model.Request;
import com.example.rooot.eatit.R;
import com.example.rooot.eatit.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class OrderListAdapter extends FirebaseRecyclerAdapter<Request, OrderViewHolder> implements ItemClickListner {

    public OrderListAdapter(@NonNull FirebaseRecyclerOptions<Request> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Request request) {
        holder.txtOrderId.setText(getRef(position).getKey());
        holder.txtOrderAddress.setText(request.getAddress());
        holder.txtOrderPhone.setText(request.getPhone());
        holder.txtOrderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
        holder.txtOrderTotalCash.setText(request.getTotal());

        holder.setOnItemClickListner(this);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.order_list_layout , parent , false));
    }

    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }
}
