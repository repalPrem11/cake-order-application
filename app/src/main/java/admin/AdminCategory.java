package admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cakeorderapp.CategoryActivity;
import com.example.cakeorderapp.HomeActivity;
import com.example.cakeorderapp.R;
import com.example.cakeorderapp.login;

import io.paperdb.Paper;

public class AdminCategory extends AppCompatActivity {
    private ImageView whiteForest, blackForest;
    private ImageView kitKat, photo;
    private ImageView flevouredCake, fondentCake;
    private ImageView annivarsaryCake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        whiteForest = findViewById(R.id.white_forest_cake);
        blackForest = findViewById(R.id.black_forest_cake);
        kitKat = findViewById(R.id.kit_kat_cake);
        photo = findViewById(R.id.photo_cake);
        flevouredCake = findViewById(R.id.flevoured_cake);
        fondentCake = findViewById(R.id.fondent_cake);
        annivarsaryCake = findViewById(R.id.annivarsary_cake);

        Paper.book().destroy();


        whiteForest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewProduct.class);
                intent.putExtra("category", "White forest cake");
                startActivity(intent);
            }
        });

        blackForest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewProduct.class);
                intent.putExtra("category", "Black forest cake");
                startActivity(intent);
            }
        });

        kitKat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewProduct.class);
                intent.putExtra("category", "Kitkat cake");
                startActivity(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewProduct.class);
                intent.putExtra("category", "Photo cake");
                startActivity(intent);
            }
        });
        flevouredCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewProduct.class);
                intent.putExtra("category", "Flavoured cake");
                startActivity(intent);
            }
        });

        fondentCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewProduct.class);
                intent.putExtra("category", "Fondant cake");
                startActivity(intent);
            }
        });

        annivarsaryCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategory.this, AdminAddNewProduct.class);
                intent.putExtra("category", "Anniversary cake");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.check_orders:
                Intent intent = new Intent(AdminCategory.this, AdminOrderCheckActivity.class);
                startActivity(intent);
                return true;


            case R.id.logout:
                Intent intent2 = new Intent(AdminCategory.this, login.class);
                Toast.makeText(this, " Successfully log out admin", Toast.LENGTH_SHORT).show();
                startActivity(intent2);
                return true;

            case R.id.edit_product:
                Intent intent3=new Intent(AdminCategory.this, CategoryActivity.class);
                intent3.putExtra("Admin", "Admin");
                startActivity(intent3);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }


    }

}