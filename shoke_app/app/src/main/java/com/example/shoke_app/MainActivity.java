package com.example.shoke_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.shoke_app.adapter.PhotoAdapter;
import com.example.shoke_app.adapter.ProductAdapter;
import com.example.shoke_app.api.ApiService;
import com.example.shoke_app.database.CartDatabase;
import com.example.shoke_app.model.Cart;
import com.example.shoke_app.model.Photo;
import com.example.shoke_app.model.Product;
import com.example.shoke_app.model.Products;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    CircleIndicator circleIndicator;
    PhotoAdapter photoAdapter;
    ArrayList<Photo> photos;
    Timer timer;
    EditText edt_search;
    ImageView img_logout, img_search, img_cart;
    Spinner spn_product;
    GridView grid_product;
    public static ArrayList<Product> products;
    public static ArrayList<Cart> carts;
    ProductAdapter productAdapter;
    public static CartDatabase database;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new CartDatabase(this, "Cart.sqlite", null, 1);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        getInit();
        setEvent();

        photos = getPhotos();
        photoAdapter = new PhotoAdapter(this, photos);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideImage();
        setSpinner();
    }

    /**
     * Mapping
     */
    public void getInit() {
        getSupportActionBar().hide();
        edt_search = findViewById(R.id.edt_search);
        img_logout = findViewById(R.id.img_logout);
        img_search = findViewById(R.id.img_search);
        img_cart = findViewById(R.id.img_cart);
        viewPager = findViewById(R.id.viewpager);
        circleIndicator = findViewById(R.id.circle_indicator);
        spn_product = findViewById(R.id.spn_product);
        grid_product = findViewById(R.id.grid_product);
        carts = new ArrayList<>();
    }

    /**
     * list of photos
     *
     * @return
     */
    public ArrayList<Photo> getPhotos() {
        ArrayList<Photo> photos = new ArrayList<>();
        photos.add(new Photo(R.drawable.banner1));
        photos.add(new Photo(R.drawable.banner2));
        photos.add(new Photo(R.drawable.banner3));
        photos.add(new Photo(R.drawable.banner4));
        photos.add(new Photo(R.drawable.banner5));
        photos.add(new Photo(R.drawable.banner6));
        return photos;
    }

    /**
     * list of product type
     *
     * @return
     */
    public ArrayList<String> getProductTypes() {
        ArrayList<String> product_types = new ArrayList<>();
        product_types.add("Tất cả sản phẩm");
        product_types.add("Laptop");
        product_types.add("Điện thoại");
        return product_types;
    }

    /**
     * Set spinner
     */
    public void setSpinner() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, getProductTypes());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_product.setAdapter(arrayAdapter);

        spn_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getProducts();
                } else {
                    getProductsByProductType(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Show all products
     */
    public void getProducts() {
        ApiService.apiService.getProducts().enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                Products data = response.body();
                if (data.isSuccess()) {
                    products = data.getProducts();
                    productAdapter = new ProductAdapter(MainActivity.this, products);
                    grid_product.setAdapter(productAdapter);
                    grid_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Product product = products.get(position);
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("PRODUCT", product);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
            }
        });

    }


    /**
     * Show products by product type
     *
     * @param index
     */
    public void getProductsByProductType(int index) {
        ApiService.apiService.getProductsByProductType(index).enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                Products data = response.body();
                if (data.isSuccess()) {
                    products = data.getProducts();
                    productAdapter = new ProductAdapter(MainActivity.this, products);
                    grid_product.setAdapter(productAdapter);
                    grid_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Product product = products.get(position);
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("PRODUCT", product);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {

            }
        });
    }

    /**
     * Set up automatic scrolling of slides
     */
    public void autoSlideImage() {
        if (photos == null || photos.isEmpty() || viewPager == null) {
            return;
        }
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = photos.size() - 1;
                        if (currentItem < totalItem) {
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3500);
    }

    /**
     * set event
     */
    public void setEvent() {
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edt_search.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Nhập tên sản phẩm tìm kiếm", Toast.LENGTH_SHORT).show();
                } else {
                    String name = edt_search.getText().toString().trim();
                    searchProduct(name);
                }
            }
        });

        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carts.clear();
                carts = database.getAllCarts(mUser.getEmail());
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

    }

    /**
     * search product by name
     * @param name
     */
    public void searchProduct(String name) {
        ApiService.apiService.getProductsByName(name).enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                Products data = response.body();
                if (data != null) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("DATA", data);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Internet Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
        super.onBackPressed();
    }
}