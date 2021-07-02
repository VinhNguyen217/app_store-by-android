package com.example.shoke_app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoke_app.adapter.ProductAdapter;
import com.example.shoke_app.model.Product;
import com.example.shoke_app.model.Products;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
     TextView tv_message;
     GridView grid_product_search;
     ProductAdapter productAdapter;
     ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getInit();
        getData();
    }

    public void getInit(){
        getSupportActionBar().setTitle("Kết quả tìm kiếm");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orange)));
        tv_message=(TextView) this.findViewById(R.id.tv_message);
        grid_product_search = findViewById(R.id.grid_product_search);
        products = new ArrayList<>();
    }

    public void getData(){
        Intent intent = getIntent();
        Products data = (Products) intent.getSerializableExtra("DATA");

        if(data.isSuccess() == true){
            products = data.getProducts();
            productAdapter = new ProductAdapter(SearchActivity.this, products);
            grid_product_search.setAdapter(productAdapter);
            grid_product_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Product product = products.get(position);
                    Intent intent = new Intent(SearchActivity.this,DetailActivity.class);
                    intent.putExtra("PRODUCT", product);
                    startActivity(intent);
                }
            });
        }else{
            tv_message.setText("Không tìm thấy sản phẩm");
        }
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