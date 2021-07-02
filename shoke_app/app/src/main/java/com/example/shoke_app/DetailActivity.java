package com.example.shoke_app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoke_app.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DetailActivity extends AppCompatActivity {

    private TextView tv_name_detail, tv_price_detail, tv_producer_detail, tv_amount_detail, tv_description_detail;
    private ImageView img_product_detail;
    private ImageButton btn_add_to_cart;
    private int price;
    private String id_product;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));

        getInit();
        getData();
        setEvent();
    }

    /**
     * Mapping
     */
    public void getInit() {
        tv_name_detail = findViewById(R.id.tv_name_detail);
        tv_price_detail = findViewById(R.id.tv_price_detail);
        tv_producer_detail = findViewById(R.id.tv_producer_detail);
        tv_amount_detail = findViewById(R.id.tv_amount_detail);
        tv_description_detail = findViewById(R.id.tv_description_detail);
        img_product_detail = findViewById(R.id.img_product_detail);
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
    }

    public void getData() {
        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra("PRODUCT");

        id_product = product.get_id();
        Picasso.get().load(product.getImg()).into(img_product_detail);

        tv_name_detail.setText(product.getName());
        getSupportActionBar().setTitle(product.getName());

        price = (int) product.getPrice();
        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(product.getPrice());
        tv_price_detail.setText("₫ " + formattedNumber);

        tv_producer_detail.setText(product.getProducer());

        tv_amount_detail.setText(String.valueOf(product.getTotal()));

        tv_description_detail.setText(String.valueOf(product.getDescription()));
    }

    public void setEvent() {
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.database.checkData(mUser.getEmail(), id_product)) {
                    Toast.makeText(DetailActivity.this, "Sản phẩm này đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    MainActivity.database.addCart(mUser.getEmail(), id_product, 1, price, null);
                    MainActivity.carts.clear();
                    MainActivity.carts = MainActivity.database.getAllCarts(mUser.getEmail());
                }
                startActivity(new Intent(DetailActivity.this, CartActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}