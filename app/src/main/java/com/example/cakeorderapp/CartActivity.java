package com.example.cakeorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Cart;
import Prevalent.Prevalent;
import ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView mTotalPrice;
    private Button mNextButton;
    private ProgressDialog loading;
    private int overAllPrice = 0;
    private ImageView mRefresh;
    private TextView msgTxt;
    private ImageView congoImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mTotalPrice = findViewById(R.id.total_price);
        mNextButton = findViewById(R.id.next_btn);
        loading = new ProgressDialog(this);
        mRefresh = findViewById(R.id.refresh_image);
        msgTxt = findViewById(R.id.congo_tv);
        congoImg = findViewById(R.id.congo_img_tv);

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTotalPrice.setText("Total Price: " + String.valueOf(overAllPrice));
                Toast.makeText(CartActivity.this, "Refreshing total price ", Toast.LENGTH_SHORT).show();

            }
        });






        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrder.class);
                intent.putExtra("Total Price", String.valueOf(overAllPrice));
                startActivity(intent);
                finish();


            }

        });

    }



    @Override
    protected void onStart() {
        super.onStart();



        CheckOrderState();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        // getting data from firebase query...
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Cakes"), Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductName.setText(model.getCakename());
                holder.txtProductQuantity.setText("Quantity :" + model.getQuantity() + " Kg");
                holder.txtProductPrice.setText("Price: " + model.getCakeprice());


                // total price
                int perProductTotalPrice = ((Integer.valueOf(model.getCakeprice()))) * Integer.valueOf(model.getQuantity());

                overAllPrice = overAllPrice + perProductTotalPrice;


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Delete"

                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                    loading.dismiss();
                                }

                                // to deleting item
                                if (i == 1) {

                                    cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Cakes").child(model.getPid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(CartActivity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(i);


                                                    }

                                                }
                                            });
                                }


                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState() {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String shippingState = snapshot.child("state").getValue().toString();
                    String userName = snapshot.child("name").getValue().toString();

                    if (shippingState.equals("confirmed")) {

                        mTotalPrice.setText("Dear " + userName + "\n order is confirmed successfully.");
                        recyclerView.setVisibility(View.GONE);
                        msgTxt.setText("Congratulations your order has been successfully confirmed.Soon you will receive your order on your door step");
                        msgTxt.setVisibility(View.VISIBLE);
                        congoImg.setVisibility(View.VISIBLE);
                        mRefresh.setVisibility(View.INVISIBLE);

                        mNextButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can order more cakes ,once you received your first order.", Toast.LENGTH_SHORT).show();

                    } else if (shippingState.equals("not shipped")) {

                        mTotalPrice.setText("Order status = Not confirmed ");
                        recyclerView.setVisibility(View.GONE);

                        msgTxt.setVisibility(View.VISIBLE);
                        congoImg.setVisibility(View.VISIBLE);
                        mRefresh.setVisibility(View.INVISIBLE);

                        mNextButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can order more cakes ,once you received your first order.", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}