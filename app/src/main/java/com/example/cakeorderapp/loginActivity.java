package com.example.cakeorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import admin.AdminCategory;
import Model.Users;
import Prevalent.Prevalent;
import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {

    private EditText demoPhoneNumber, demoPassword;
   private Button demoLoginButton;
    private ProgressDialog loadingBAr;
    private String parentDBname = "Users";
    private TextView AdminTv, NotAdminTv,registerScreen,forgotPassword;

    private CheckBox chkboxRememberme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        getSupportActionBar().hide();

        demoPhoneNumber = findViewById(R.id.login_phone_number_input);
        demoPassword = findViewById(R.id.login_password_input);
        demoLoginButton = findViewById(R.id.login_btn);
        loadingBAr = new ProgressDialog(this);
        AdminTv = findViewById(R.id.admin_panel_link);
        NotAdminTv = findViewById(R.id.not_admin_panel_link);
        chkboxRememberme = findViewById(R.id.remember_me_chkbtn);
        registerScreen=findViewById(R.id.register_tv);
        forgotPassword=findViewById(R.id.forget_password_tv);


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(loginActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });

        registerScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(loginActivity.this,registerActivity.class);
                startActivity(intent);
            }
        });

        Paper.init(this);                                       // library


        demoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        AdminTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoLoginButton.setText("Login Admin");
                AdminTv.setVisibility(View.INVISIBLE);
                NotAdminTv.setVisibility(View.VISIBLE);
                parentDBname = "Admins";

            }
        });

        NotAdminTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoLoginButton.setText("Login");
                AdminTv.setVisibility(View.VISIBLE);
                NotAdminTv.setVisibility(View.INVISIBLE);
                parentDBname = "Users";

            }
        });
    }


    private void LoginUser() {
        String phonenumber = demoPhoneNumber.getText().toString();
        String password = demoPassword.getText().toString();

        if (TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(this, "please enter your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "please enter your password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBAr.setTitle("Login Account");
            loadingBAr.setMessage("please wait, while we are checking the credentials");
            loadingBAr.setCanceledOnTouchOutside(false);
            loadingBAr.show();


            AllowAccessTheAccount(phonenumber, password);
        }
    }

    private void AllowAccessTheAccount(String phonenumber, String password) {

        if (chkboxRememberme.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phonenumber);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(parentDBname).child(phonenumber).exists()) {

                    Users usersdata = snapshot.child(parentDBname).child(phonenumber).getValue(Users.class);

                    if (usersdata.getPhone().equals(phonenumber)) {

                        if (usersdata.getPassword().equals(password)) {

                            if (parentDBname.equals("Admins")) {

                                Toast.makeText(loginActivity.this, "Logged in successfully as a Admin", Toast.LENGTH_SHORT).show();
                                loadingBAr.dismiss();

                                Intent intent = new Intent(loginActivity.this, AdminCategory.class);
                                startActivity(intent);
                            } else if (parentDBname.equals("Users")) {
                                Toast.makeText(loginActivity.this, "Logged in successfully..", Toast.LENGTH_SHORT).show();
                                loadingBAr.dismiss();

                                   Intent intent=new Intent(loginActivity.this,HomeActivity.class);
                                   Prevalent.CurrentOnlineUser=usersdata;
                                 startActivity(intent);
                            }


                        } else {
                            loadingBAr.dismiss();
                            Toast.makeText(loginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(loginActivity.this, "Account with this " + phonenumber + " number do not exists", Toast.LENGTH_SHORT).show();
                    loadingBAr.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}