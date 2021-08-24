package admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cakeorderapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.AdminOrders;

public class AdminOrderCheckActivity extends AppCompatActivity {

    private RecyclerView orderList;
    DatabaseReference orderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_check);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        getSupportActionBar().hide();

        orderList = findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // for showing orders on recycler view......

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(orderRef, AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders, AdminViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, AdminViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull AdminViewHolder holder, int position, @NonNull AdminOrders model) {

                holder.userName.setText("Name: "+model.getName());
                holder.userPhoneNumber.setText("Phone: "+model.getPhone());
                holder.userShippingAddress.setText("Shipping Address: "+model.getAddress());
                holder.userDateTime.setText("Order at: "+model.getDate() + " "+model.getTime());
                holder.userTotalPrice.setText("Total Amount: "+model.getTotalAmount());
                holder.cakeMessage.setText("Message on Cake: "+model.getCakemessage());


                // order list button pg
                holder.showOrderCake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uId=getRef(position).getKey();

                        Intent intent=new Intent(AdminOrderCheckActivity.this, AdminUserProductActivity.class);
                        intent.putExtra("uid",uId);
                        startActivity(intent);
                    }
                });

                // admin side for  confirming order
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminOrderCheckActivity.this);
                        builder.setTitle("Have you shipped this order cake ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if (i==0){

                                    String uId=getRef(position).getKey();
                                    RemoveOrder(uId);
                                }
                                else {
                                    finish();
                                }

                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new AdminViewHolder(view);
            }
        };

        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uId) {
        orderRef.child(uId).removeValue();



    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder {

        public TextView userName, cakeMessage, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress;
        public Button showOrderCake;

        public AdminViewHolder(@NonNull View itemView) {

            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            cakeMessage = itemView.findViewById(R.id.order_cake_message);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_address);
            showOrderCake = itemView.findViewById(R.id.order_show_all_products_btn);
        }
    }
}