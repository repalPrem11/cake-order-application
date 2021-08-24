package admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cakeorderapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProduct extends AppCompatActivity {

    private String CategoryName,PDescription,Pname,Pprice,saveCurrentDate,saveCurrentTime;
    private ImageView addImage;
    private EditText productName,productDescription,productPrice;
    private Button addProduct;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private String productRandomKey,downloadImageUrl;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        getSupportActionBar().hide();
        ProductImageRef= FirebaseStorage.getInstance().getReference().child("cakeimages");
        ProductRef=FirebaseDatabase.getInstance().getReference().child("Cakes");
        loading= new ProgressDialog(this);

        CategoryName=getIntent().getExtras().get("category").toString();              //  showing cake type from admin category activity toast
        Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();

        addImage=findViewById(R.id.select_product_image);
        productName=findViewById(R.id.product_name);
        productDescription=findViewById(R.id.product_discription);
        productPrice=findViewById(R.id.product_price);
        addProduct=findViewById(R.id.add_product);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });




    }

    private void OpenGallery() {
        Intent galleyIntent=new Intent();
        galleyIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleyIntent.setType("image/*");
        startActivityForResult(galleyIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            ImageUri=data.getData();
            addImage.setImageURI(ImageUri);

        }
    }

    private void ValidateProductData(){
        PDescription=productDescription.getText().toString();
        Pname=productName.getText().toString();
        Pprice=productPrice.getText().toString();

        if (ImageUri==null){
            Toast.makeText(this, "please select cake image", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(PDescription)){
            Toast.makeText(this, "please enter cake description", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname)){
            Toast.makeText(this, "please enter cake name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pprice)){
            Toast.makeText(this, "please enter cake price", Toast.LENGTH_SHORT).show();
        }

        else{
            StoreProductInforamtion();
        }

    }

    private void StoreProductInforamtion() {

        loading.setTitle("Add new product");
        loading.setMessage("Admin please wait, while we are adding new product");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());


        productRandomKey= saveCurrentDate +  saveCurrentTime;


       final StorageReference filePath=ProductImageRef.child(ImageUri.getLastPathSegment() + productRandomKey+".jpg");

        final UploadTask uploadTask=filePath.putFile(ImageUri);



        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message=e.toString();
                Toast.makeText(AdminAddNewProduct.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                loading.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProduct.this, "Cake Image added successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {


                        if (!task.isSuccessful()){
                            throw task.getException();

                        }


                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl=task.getResult().toString();

                            Toast.makeText(AdminAddNewProduct.this, "got the Cake image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String,Object> productMap=new HashMap<>();

        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",PDescription);
        productMap.put("cakeimage",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",Pprice);
        productMap.put("cakename",Pname);

        ProductRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent=new Intent(AdminAddNewProduct.this, AdminCategory.class);
                    startActivity(intent);
                    loading.dismiss();

                    Toast.makeText(AdminAddNewProduct.this, "Product is added is successfully...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loading.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(AdminAddNewProduct.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}