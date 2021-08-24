package com.example.cakeorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Model.Products;
import ViewHolder.ProductViewHolder;

public class SearchProductActivity extends AppCompatActivity {

    private EditText inputText;
    private ImageView searchButton;
    private RecyclerView searchList;
    private String SearchInput;
   // ArrayList<Products> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        getSupportActionBar().hide();

        inputText=findViewById(R.id.search_product);
        searchButton=findViewById(R.id.search_btn);
        searchList=findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this));
        searchList.hasFixedSize();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchInput= inputText.getText().toString();

                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Cakes");

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(reference.orderByChild("description").startAt(SearchInput),Products.class).build();

        FirebaseRecyclerAdapter<Products, myCategoryAdapter.MyViewHolder>adapter=new FirebaseRecyclerAdapter<Products, myCategoryAdapter.MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myCategoryAdapter.MyViewHolder holder, int position, @NonNull Products model) {



                holder.name.setText(model.getCakename());
                holder.description.setText(model.getDescription());
                holder.price.setText(model.getPrice());
                Glide.with(holder.img.getContext()).load(model.getCakeimage()).into(holder.img);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(SearchProductActivity.this,ProductDetailsActivity.class);
                        intent.putExtra("pid",model.getPid());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public myCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.categorylayout, parent, false);
                return new myCategoryAdapter.MyViewHolder(v);
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();




    }
}