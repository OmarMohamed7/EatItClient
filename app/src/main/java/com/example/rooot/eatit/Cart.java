package com.example.rooot.eatit;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rooot.eatit.Common.Common;
import com.example.rooot.eatit.Database.Database;
import com.example.rooot.eatit.Helper.RecyclerItemTouchHelper;
import com.example.rooot.eatit.Interface.RecyclerItemHelperListner;
import com.example.rooot.eatit.Model.Order;
import com.example.rooot.eatit.Model.Request;
import com.example.rooot.eatit.Adapter.CartAdapter;
import com.example.rooot.eatit.ViewHolder.CartViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity implements RecyclerItemHelperListner {

    RecyclerView recyclerView;
    FrameLayout c_rootLayout;

    DatabaseReference request_ref;

    TextView txtTotalPrice;
    FButton btnPlaceOrder;

    List<Order> cart = new ArrayList<>();
    CartAdapter cartAdapter;

    static int total2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_layout);

        request_ref = FirebaseDatabase.getInstance().getReference("Requests");

        c_rootLayout = findViewById(R.id.root_layout);
        recyclerView = findViewById(R.id.listCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this , DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack =
                new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT&ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);

        txtTotalPrice = findViewById(R.id.totalCash);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cart.size() == 0){
                    Toast toast = Toast.makeText(Cart.this, "You must make an order", Toast.LENGTH_SHORT);
                    if(toast != null)
                        toast.show();
                }else{
                    showAlertDialog();
                }


            }
        });

        //new Database(this).CleanCart();

        LoadListFood();

    }

    private void showAlertDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(Cart.this)
                .setMessage("Enter your address : ")
                .setTitle("One more step !");

        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(lp);
        dialog.setView(edtAddress);
        dialog.setIcon(R.drawable.ic_add_shopping_cart_black_24dp);

        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request request = new Request(
                        Common.current_user.getPhone(),
                        Common.current_user.getName(),
                        edtAddress.getText().toString(),
                        String.valueOf(total2),
                        cart
                );


                // Make the key the time of system
                request_ref.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                //delete the cart
                new Database(Cart.this).CleanCart();

                Toast.makeText(Cart.this, "Thank you, Order submitted successfully", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();

    }

    private void LoadListFood() {

        cart = new Database(this).getCarts();

        cartAdapter = new CartAdapter(cart , this);
        recyclerView.setAdapter(cartAdapter);

        // Calculate the total Number
        updateTotalCash();

    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder holder, int direction, final int position) {

        String name = ((CartAdapter)recyclerView.getAdapter()).getItem(position).getProductName();
        String id = ((CartAdapter)recyclerView.getAdapter()).getItem(position).getProductId();

        final Order deleteItem = ((CartAdapter)recyclerView.getAdapter()).getItem(holder.getAdapterPosition());
        final int deleteIndex = holder.getAdapterPosition();

        if(holder instanceof CartViewHolder){

            cartAdapter.removeItem(deleteIndex);

            new Database(this).removeItem(deleteItem);

            cartAdapter.notifyItemRemoved(position);

            updateTotalCash();

            Snackbar snackbar = Snackbar.make(c_rootLayout , ""+id+" "+name+" Removed " ,Snackbar.LENGTH_LONG )
                    .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new Database(getBaseContext()).addToCart(deleteItem);
                                cartAdapter.restoreItem(deleteItem,deleteIndex);
                                updateTotalCash();
                            }
                    });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }


        }

    public void updateTotalCash(){
        int total = 0;
        for(Order order : cart){
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        }

        total2 = total;

        Locale local = new Locale("en" , "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(local);

        txtTotalPrice.setText(fmt.format(total));
    }
}
