package com.example.shoke_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoke_app.CartActivity;
import com.example.shoke_app.DetailActivity;
import com.example.shoke_app.MainActivity;
import com.example.shoke_app.R;
import com.example.shoke_app.api.ApiService;
import com.example.shoke_app.model.Cart;
import com.example.shoke_app.model.Product;
import com.example.shoke_app.model.Products;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Cart> cartList;

    public CartAdapter(Context context, ArrayList<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        if (cartList != null)
            return cartList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (cartList != null)
            return cartList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (cartList != null)
            return position;
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_cart, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.img_product_cart = convertView.findViewById(R.id.img_product_cart);
            viewHolder.name_cart = convertView.findViewById(R.id.name_product_cart);
            viewHolder.price_cart = convertView.findViewById(R.id.price_product_cart);
            viewHolder.tv_value = convertView.findViewById(R.id.tv_value);
            viewHolder.btn_minus = convertView.findViewById(R.id.btn_minus);
            viewHolder.btn_plus = convertView.findViewById(R.id.btn_plus);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_value.setText(String.valueOf(cartList.get(position).getCount()));

        ApiService.apiService.getProductsById(cartList.get(position).getIdProduct()).enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                Products data = response.body();
                Product product = data.getProducts().get(0);
                if (data.isSuccess()) {
                    Picasso.get().load(product.getImg()).into(viewHolder.img_product_cart);
                    viewHolder.name_cart.setText(product.getName());
                    NumberFormat formatter = new DecimalFormat("#,###");
                    String formattedNumber = formatter.format(product.getPrice());
                    viewHolder.price_cart.setText("₫ " + formattedNumber);
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Toast.makeText(context, "Connect Error", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.img_product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService.apiService.getProductsById(cartList.get(position).getIdProduct()).enqueue(new Callback<Products>() {
                    @Override
                    public void onResponse(Call<Products> call, Response<Products> response) {
                        Products data = response.body();
                        if (data != null) {
                            Product product = data.getProducts().get(0);
                            Intent intent = new Intent(context, DetailActivity.class);
                            intent.putExtra("PRODUCT", product);
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Products> call, Throwable t) {
                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        viewHolder.btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(viewHolder.tv_value.getText().toString());
                if (value > 1) {
                    value--;
                }
                viewHolder.tv_value.setText(String.valueOf(value));
                MainActivity.database.UpdateCart(value, cartList.get(position).getId());
                MainActivity.carts.get(position).setCount(value);
                CartActivity.setPriceCart();
            }
        });

        viewHolder.btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(viewHolder.tv_value.getText().toString());
                value++;
                viewHolder.tv_value.setText(String.valueOf(value));
                MainActivity.database.UpdateCart(value, cartList.get(position).getId());
                MainActivity.carts.get(position).setCount(value);
                CartActivity.setPriceCart();
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView img_product_cart;
        TextView name_cart;
        TextView price_cart;
        TextView tv_value;
        ImageButton btn_minus;
        ImageButton btn_plus;
    }
}
