package com.example.cakeorderapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

import Model.Products;
import admin.AdminMaintainProductsActivity;

public class myCategoryAdapter extends RecyclerView.Adapter<myCategoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<Products> list;
    private String type="";



    public myCategoryAdapter(Context context, ArrayList<Products> list) {
        this.context = context;
        this.list = list;
    }

    public myCategoryAdapter(FirebaseRecyclerOptions<Products> options) {
        super();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.categorylayout, parent, false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Products pro = list.get(position);
        holder.name.setText(pro.getCakename());
        holder.description.setText(pro.getDescription());
        holder.price.setText(pro.getPrice());
        Glide.with(holder.img.getContext()).load(pro.getCakeimage()).into(holder.img);








        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equals("Admin")){

                    Intent intent=new Intent(context, AdminMaintainProductsActivity.class);
                    intent.putExtra("pid",pro.getPid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
                else {
                    Intent intent=new Intent(context,ProductDetailsActivity.class);
                    intent.putExtra("pid",pro.getPid());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, price, description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img1);
            name = itemView.findViewById(R.id.cake_name);
            price = itemView.findViewById(R.id.cake_price);
            description = itemView.findViewById(R.id.cake_description);


        }
    }
}
