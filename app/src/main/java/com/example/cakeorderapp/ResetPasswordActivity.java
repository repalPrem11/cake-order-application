package com.example.cakeorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import Prevalent.Prevalent;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, bigTv;
    private EditText mPhoneNumber, mQuestion1, mQuestion2;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().hide();


        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.page_title);
        bigTv = findViewById(R.id.tvvv);
        mPhoneNumber = findViewById(R.id.find_phone_Number);
        mQuestion1 = findViewById(R.id.question_one);
        mQuestion2 = findViewById(R.id.question_two);
        verifyButton = findViewById(R.id.verify_btn);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // if user want to set the questions from settings.....

        mPhoneNumber.setVisibility(View.GONE);

        if (check.equals("settings")) {
            pageTitle.setText("Set Answers");
            bigTv.setText("Please set Answers for the following Security Questions");

            verifyButton.setText("Set");


            displayAnswers();

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    setQuestionAnswer();


                }

            });

        }


        // if user press the forgot password text....

        else if (check.equals("login")) {

            mPhoneNumber.setVisibility(View.VISIBLE);
            
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });

        }
    }

    private void verifyUser() {
        String phone = mPhoneNumber.getText().toString();

        String answer1 = mQuestion1.getText().toString().toLowerCase();
        String answer2 = mQuestion2.getText().toString().toLowerCase();



        if (!phone.equals("")&& !answer1.equals("")&& !answer2.equals("")){

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){

                        // getting phone number valuew from firebase

                        String mPhone=snapshot.child("phone").getValue().toString();

                        if (snapshot.hasChild("Security Questions")){

                            String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 =snapshot.child("Security Questions").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1)){
                                Toast.makeText(ResetPasswordActivity.this, "Your first answer is wrong", Toast.LENGTH_SHORT).show();
                            }
                            else if (!ans2.equals(answer2)){

                                Toast.makeText(ResetPasswordActivity.this, "Your second answer is wrong", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this,R.style.AlertDialogStyle);
                                builder.setTitle("New Password");

                                final EditText newPassword=new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write password here...");
                                builder.setView(newPassword);



                                // for options in alert dialog

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {


                                        if (newPassword.getText().toString().equals("")){
                                            Toast.makeText(ResetPasswordActivity.this, "Enter password", Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            ref.child("password").setValue(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()){
                                                        Toast.makeText(ResetPasswordActivity.this, "Password changed successfully...", Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(ResetPasswordActivity.this,loginActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                       dialog.cancel();

                                    }
                                });
                                builder.show();
                            }

                        }
                        else {
                            Toast.makeText(ResetPasswordActivity.this, "This user phone number not exists", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this, "This phone number not exits", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else {
            Toast.makeText(this, "please complete the form", Toast.LENGTH_SHORT).show();
        }
       


    }


    // method for settings answers of questions

    private void setQuestionAnswer() {


        String question1 = mQuestion1.getText().toString().toLowerCase();
        String question2 = mQuestion2.getText().toString().toLowerCase();

        if (TextUtils.isEmpty(question1)) {
            mQuestion1.setError("please answer the questions");
        } else if (TextUtils.isEmpty(question2)) {
            mQuestion2.setError("please answer the question");
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

            // for storing answers into the database

            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("answer1", question1);
            hashMap.put("answer2", question2);

            ref.child("Security Questions").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "You have set the answers successfully..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }

    private void displayAnswers() {

        // if user already set the answer then user can see there answers

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.CurrentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String answer1 = snapshot.child("answer1").getValue().toString();
                    String answer2 = snapshot.child("answer2").getValue().toString();

                    mQuestion1.setText(answer1);
                    mQuestion2.setText(answer2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}