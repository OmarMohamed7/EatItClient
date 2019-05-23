package com.example.rooot.eatit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rooot.eatit.Adapter.FirebaseAdapter;
import com.example.rooot.eatit.Common.Common;
import com.example.rooot.eatit.Interface.FirebaseCallBack;
import com.example.rooot.eatit.Model.Category;
import com.example.rooot.eatit.Model.Order;
import com.example.rooot.eatit.Service.OrdersListner;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FirebaseCallBack, OnItemSelectedListener {

    Context context;

    DatabaseReference category;

    TextView txtFullName, txtMenuString;

    RecyclerView recyclerMenu;
    FirebaseAdapter firebaseAdapter;

    Spinner spinner;

    Category model;

    ArrayList<String> LIST_CATEGORIES, SAVEDLIST_CATEGORIES , SAVEDLIST_ID ;
    ArrayAdapter<String> adapterSpinner;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        txtMenuString = findViewById(R.id.txtMenu);
        SAVEDLIST_CATEGORIES = new ArrayList<>();
        SAVEDLIST_ID = new ArrayList<>();

        model = new Category();
        
        firebase(this);

        //Spinner
        this.spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        adapterSpinner = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, SAVEDLIST_CATEGORIES);
        spinner.setVisibility(View.VISIBLE);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterSpinner);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseAdapter != null) {
                    Intent intent = new Intent(Home.this, Cart.class);
                    startActivity(intent);
                }else
                    Toast.makeText(context, "Waiting for data !", Toast.LENGTH_SHORT).show();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get the header view
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.current_user.getName());

        recyclerMenu = findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this));

        //Register Service
        Intent intent = new Intent(Home.this , OrdersListner.class);
        startService(intent);

    }

    //save categories into arrayList to show it in spinner
    // we made an interface with extra call back to avoid that the data outside value event
    //  listner is null
    private void firebase(final FirebaseCallBack firebaseCallBack) {

        category = FirebaseDatabase.getInstance().getReference("Category");

        category.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LIST_CATEGORIES = new ArrayList<>();
                for (DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {
                    LIST_CATEGORIES.add(postSnapshot1.getKey());

                }

                firebaseCallBack.SaveCategories(LIST_CATEGORIES); // we made an interface becuase LIST_CATEGORIES outside onDataChange
                // is always null so we need a custom call to save data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadMenu(String selectedItem) {

        Query query = FirebaseDatabase.getInstance().getReference("Category/"+selectedItem);

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, Category.class)
                        .build();

        firebaseAdapter = new FirebaseAdapter(options);

        recyclerMenu.setAdapter(firebaseAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapterSpinner.notifyDataSetChanged();

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAdapter.stopListening();
        adapterSpinner.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    // We will Override onPause Method to save the position of selected item in spinner
    // so that when we get back fro another activityit will call on resume method
    // and the Firebaseadapter will be null so we will call it with our position

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("SPINNER_ITEM" , MODE_PRIVATE);
        sharedPreferences.edit().putInt("SPINNER_ITEM_INDEX" , spinner.getSelectedItemPosition()).apply();

        firebaseAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(flag) {
            SharedPreferences sharedPreferences = getSharedPreferences("SPINNER_ITEM", MODE_PRIVATE);

            int indx;

            if (sharedPreferences != null) {
                indx = sharedPreferences.getInt("SPINNER_ITEM_INDEX", 0);

                // We must set the adapter again to spinner
                // index to return to the category we were selected
                spinner.setAdapter(adapterSpinner);
                spinner.setSelection(indx);

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_cart){

            Intent cartIntent = new Intent(Home.this , Cart.class);
            startActivity(cartIntent);

        }else if(id == R.id.nav_order){

            Intent orderIntent = new Intent(Home.this , OrderStatus.class);
            startActivity(orderIntent);

        }else if(id == R.id.nav_manage){

        }else if(id == R.id.nav_logout){

            Intent signInIntent = new Intent(Home.this,SignIn.class);
            signInIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signInIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //this function is in iterface to save the Categoris of foods
    public void SaveCategories(List<String> list){

        for(int i=0; i<list.size();i++)
            SAVEDLIST_CATEGORIES.add(i,list.get(i));

        adapterSpinner.notifyDataSetChanged();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String name = adapterView.getSelectedItem().toString();

        loadMenu(name);
        firebaseAdapter.startListening();

        // this flag to work with onResume
        // so the first time of application we want onResume to do anything so w put default value false
        // then after getting Categories we will put flag true so if we transfered to second activity
        // it will call onPaused then when we hit back it will call onResume with flag true ^ ^
        flag = true;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
