package com.example.cakeorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import Prevalent.Prevalent;

public class ConfirmFinalOrder extends AppCompatActivity {

    private EditText mNameEditText,mCakeMessageEditText,mPhoneNumberEditText,mAddressEditText,mCityEditText;
    private Button mConfirmOrder;

    private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        mNameEditText=findViewById(R.id.edit_text_name);
        mCakeMessageEditText=findViewById(R.id.edit_text_cake_name);
        mPhoneNumberEditText=findViewById(R.id.edit_text_phone);
        mAddressEditText=findViewById(R.id.edit_text_address);
        mConfirmOrder=findViewById(R.id.confirm_order_btn);
        mCityEditText=findViewById(R.id.edit_text_city);

        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price  Rs." +totalAmount, Toast.LENGTH_SHORT).show();

        mConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });


    }

    private void Check() {

        if (TextUtils.isEmpty(mNameEditText.getText().toString())){
            mNameEditText.setError("Please provide full name");
        }
        else if (TextUtils.isEmpty(mCakeMessageEditText.getText().toString())){
            mCakeMessageEditText.setError("please provide message on cake");
        }
        else if (TextUtils.isEmpty(mPhoneNumberEditText.getText().toString())){
            mPhoneNumberEditText.setError("Please provide your phone number");
        }
        else if (TextUtils.isEmpty(mAddressEditText.getText().toString())){
            mAddressEditText.setError("Please provide correct home address");
        }
        else if (TextUtils.isEmpty(mCityEditText.getText().toString())){
            mCityEditText.setError("Please provide your city name");
        }
        else {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String SaveCurrentTime,SaveCurrentDate;

        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        SaveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        SaveCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference orders= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.CurrentOnlineUser.getPhone());

        // for adding data to firebase
        HashMap<String,Object> orderMap=new HashMap<>();

        orderMap.put("totalAmount",totalAmount);
        orderMap.put("name",mNameEditText.getText().toString());
        orderMap.put("phone",mPhoneNumberEditText.getText().toString());
        orderMap.put("address",mAddressEditText.getText().toString());
        orderMap.put("city",mCityEditText.getText().toString());
        orderMap.put("date",SaveCurrentDate);
        orderMap.put("time",SaveCurrentTime);
        orderMap.put("cakemessage",mCakeMessageEditText.getText().toString());
        orderMap.put("state","not shipped");


        orders.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.CurrentOnlineUser.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrder.this, "Final order has been submitted", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(ConfirmFinalOrder.this,HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }

                                }
                            });
                }

            }
        });
    }
}