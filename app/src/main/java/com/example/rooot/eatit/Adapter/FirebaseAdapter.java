package com.example.rooot.eatit.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rooot.eatit.Database.Database;
import com.example.rooot.eatit.Interface.ItemClickListner;
import com.example.rooot.eatit.Model.Category;
import com.example.rooot.eatit.Model.Order;
import com.example.rooot.eatit.R;
import com.example.rooot.eatit.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FirebaseAdapter extends FirebaseRecyclerAdapter<Category,MenuViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseAdapter(@NonNull FirebaseRecyclerOptions<Category> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position, @NonNull Category model) {
        holder.txtMenuName.setText(model.getName());
        holder.txtMenuHint.setText(model.getHint());
        holder.txtMenuPrice.setText(model.getPrice());

        Picasso.with(holder.imgMenu.getContext()).load(model.getLink()).into(holder.imgMenu);

        final Category clickedItem = model;

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                getRef(position).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                        current_food = dataSnapshot.getValue(Category.class);
                        // make a new Order
                        new Database(view.getContext()).
                                addToCart(new Order(getRef(position).getKey(),
                                                    clickedItem.getName(),
                                                    holder.quantityNumberButton.getNumber(),
                                                    clickedItem.getPrice()));

                        Toast.makeText(view.getContext(), "Added To Cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MenuViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_item , viewGroup , false));

    }






}
