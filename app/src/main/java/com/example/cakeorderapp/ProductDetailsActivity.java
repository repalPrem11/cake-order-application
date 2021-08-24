package com.example.cakeorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Model.Products;
import Prevalent.Prevalent;

public class ProductDetailsActivity extends AppCompatActivity {


    private TextView mProductName, mProductDescription, mProductPrice, contactUsTV;
    private ImageView mProductImage;
    private RadioButton mHalfKg, mOneKg, mTwoKg, mThreeKg, mFourKg;
    public String productId = "", state = "Normal";
    private Button addToCart;


    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().hide();


        productId = getIntent().getStringExtra("pid");


        mProductName = findViewById(R.id.product_name_details);
        mProductDescription = findViewById(R.id.product_description_details);
        mProductPrice = findViewById(R.id.product_price_details);
        contactUsTV = findViewById(R.id.contact_us_details);
        mProductImage = findViewById(R.id.product_image_details);

        mOneKg = findViewById(R.id.one_radio_button_details);
        mTwoKg = findViewById(R.id.two_radio_button_details);
        mFourKg = findViewById(R.id.four_radio_button_details);
        mThreeKg = findViewById(R.id.three_radio_button_details);
        addToCart = findViewById(R.id.save_cart_btn_details);

        contactUsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });


        getProductDetails(productId);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (state.equals("Order Placed") || state.equals("Order shipped")) {
                    Toast.makeText(ProductDetailsActivity.this, "You can order more cakes, once your previous order is shipped or confirmed", Toast.LENGTH_LONG).show();
                }
                else {
                    addingCart();

                }
            }
        });

    }

    private void addingCart() {


        String SaveCurrentTime, SaveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        SaveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime = currentTime.format(calForDate.getTime());


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        Products products = new Products();


        String one = mOneKg.getText().toString();
        String two = mTwoKg.getText().toString();
        String three = mThreeKg.getText().toString();
        String four = mFourKg.getText().toString();


          if (mOneKg.isChecked()) {
            products.setKg(one);
            cartListRef.child("Cart List").setValue("1");

        } else if (mTwoKg.isChecked()) {
            products.setKg(two);
            cartListRef.child("Cart List").setValue("2");


        } else if (mThreeKg.isChecked()) {
            products.setKg(three);
            cartListRef.child("Cart List").setValue("3");

        } else if (mFourKg.isChecked()) {
            products.setKg(four);
            cartListRef.child("Cart List").setValue("4");
        } else {
            Toast.makeText(ProductDetailsActivity.this, "Please select Kg", Toast.LENGTH_SHORT).show();

        }


        final HashMap<String, Object> cartMap = new HashMap<>();


        cartMap.put("pid", productId);
        cartMap.put("cakename", mProductName.getText().toString());
        cartMap.put("cakeprice", mProductPrice.getText().toString());
        cartMap.put("date", SaveCurrentDate);
        cartMap.put("time", SaveCurrentTime);
        cartMap.put("quantity", products.getKg());
        cartMap.put("discount", "");
       // cartMap.put("cakeimage",mProductImage);



                    cartListRef.child("Admin View").child(Prevalent.CurrentOnlineUser.getPhone()).child("Cakes").child(productId).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            if (task.isSuccessful()) {
                                Toast.makeText(ProductDetailsActivity.this, "Added to cart list", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }


                        }
                    });




    }


    private void getProductDetails(String productId) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Cakes");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    Products products = snapshot.getValue(Products.class);

                    mProductName.setText(products.getCakename());
                    mProductDescription.setText(products.getDescription());
                    mProductPrice.setText(products.getPrice());
                    Picasso.get().load(products.getCakeimage()).into(mProductImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckOrderState() {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    String shippingState = snapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        state = "Order shipped";
                    } else if (shippingState.equals("not shipped")) {

                        state = "Order Placed";
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}