package com.example.cakeorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Model.Products;
import admin.AdminCategory;
import admin.AdminOrderCheckActivity;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recview;
    DatabaseReference database;
    myCategoryAdapter myAdapter;
    ArrayList<Products> list;

    private String type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //to check user or admin

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){

            type=getIntent().getExtras().get("Admin").toString();
        }


        recview = findViewById(R.id.recView);
        recview.setLayoutManager(new LinearLayoutManager(this));
        recview.setHasFixedSize(true);
        database = FirebaseDatabase.getInstance().getReference().child("Cakes");

        list = new ArrayList<>();
        myAdapter = new myCategoryAdapter(this, list);
        recview.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Products products = dataSnapshot.getValue(Products.class);
                    list.add(products);
                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.categorymenu, menu);


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search_category:
                Intent intent = new Intent(CategoryActivity.this, SearchProductActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}