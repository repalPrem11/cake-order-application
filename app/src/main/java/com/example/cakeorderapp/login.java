package com.example.cakeorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class login extends AppCompatActivity {

    private Button joinNow;
    private Button registerNow;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        joinNow=findViewById(R.id.login_button);
        registerNow=findViewById(R.id.register_btn);
        loadingBar=new ProgressDialog(this);

        Paper.init(this);

        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,loginActivity.class);
                startActivity(intent);
            }
        });

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,registerActivity.class);
                startActivity(intent);
            }
        });


        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordkey=Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey!=""  && UserPasswordkey!=""){
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordkey)){

                AllowAccess(UserPhoneKey,UserPasswordkey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Be patience");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }




    }

    private void AllowAccess(String phonenumber, String password) {


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child("Users").child(phonenumber).exists()){

                    Users usersdata=snapshot.child("Users").child(phonenumber).getValue(Users.class);

                    if (usersdata.getPhone().equals(phonenumber)){

                        if (usersdata.getPassword().equals(password)){
                            Toast.makeText(login.this, "Logged in successfully..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent=new Intent(login.this,HomeActivity.class);
                            Prevalent.CurrentOnlineUser=usersdata;
                            startActivity(intent);
                        }
                    }

                }
                else {
                    Toast.makeText(login.this, "Account with this "+phonenumber+" number do not exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}