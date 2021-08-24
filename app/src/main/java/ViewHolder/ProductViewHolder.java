package ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cakeorderapp.ProductDetailsActivity;
import com.example.cakeorderapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Products;

public class ProductViewHolder extends RecyclerView.Adapter<ProductViewHolder.ImageViewHolder>  {

    private Context mContext;
    private List<Products>mProducts;
    private String type="";







    public ProductViewHolder(Context context,List<Products>products){

        mContext=context;
        mProducts=products;




    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v= LayoutInflater.from(mContext).inflate(R.layout.product_items_layout,parent,false);

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Products uploadCurrent=mProducts.get(position);


        holder.productNameTextView.setText(uploadCurrent.getCakename());
        holder.productDescriptionTextView.setText(uploadCurrent.getDescription());
        holder.productPriceTextView.setText("Price: "+uploadCurrent.getPrice());
        //Picasso.get().load(uploadCurrent.getCakeimage()).fit().centerCrop().into(holder.imageView);
        //Glide.with(mContext).load(uploadCurrent.getCakeimage()).centerCrop().into(holder.imageView);
        Picasso.get().load(uploadCurrent.getCakeimage()).into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (type.equals("admin")){

                }
                else {
                    Intent intent=new Intent(mContext,ProductDetailsActivity.class);
                    intent.putExtra("pid",uploadCurrent.getPid());
                    mContext.startActivity(intent);

                }





            }
        });





    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder{

        TextView productNameTextView,productDescriptionTextView,productPriceTextView;
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            productNameTextView=itemView.findViewById(R.id.product_namee);
            productDescriptionTextView=itemView.findViewById(R.id.product_descriptionn);
            productPriceTextView=itemView.findViewById(R.id.product_pricee);
            imageView=itemView.findViewById(R.id.product_imagee);
        }
    }





    }












