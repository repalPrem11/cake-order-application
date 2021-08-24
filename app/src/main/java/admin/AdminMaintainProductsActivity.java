package admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.cakeorderapp.R;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private EditText pName,pDescription,pPrice;
    private Button mApplyChanges;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        pName=findViewById(R.id.admin_product_name);
        pDescription=findViewById(R.id.admin_product_descriptionn);
        pPrice=findViewById(R.id.admin_product_pricee);
        mApplyChanges=findViewById(R.id.admin_apply_changes_btn);
        imageView=findViewById(R.id.admin_product_imagee);
    }
}