package com.example.rooot.eatit;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rooot.eatit.Adapter.OrderListAdapter;
import com.example.rooot.eatit.Common.Common;
import com.example.rooot.eatit.Model.Request;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class OrderStatus extends AppCompatActivity{

    public RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference requests;

    OrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //if we started out OrderStatus from home then we will not put any extra
        if(getIntent().getExtras() == null)
            loadOrders(Common.current_user.getPhone());
        else
            loadOrders(getIntent().getStringExtra("userPhone"));

    }

    private void loadOrders(String phone) {

        Query query = FirebaseDatabase.getInstance().getReference("Requests");

        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(query.orderByChild("phone").equalTo(phone) , Request.class)
                .build();

        adapter = new OrderListAdapter(options);

        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


}
