package com.example.shoke_app.api;

import com.example.shoke_app.model.Products;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    //https://fastandfurious217.herokuapp.com/product

    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("https://fastandfurious217.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    //Get the list of products
    @GET("product")
    Call<Products> getProducts();

    //https://fastandfurious217.herokuapp.com/product/sortout/?sortOut=1
    //Get the list of products by product type
    @GET("product/sortout")
    Call<Products> getProductsByProductType(@Query("sortOut") int sortOut);


    //https://fastandfurious217.herokuapp.com/product/search?_id=60dd8b37fb53460015f99f29
    @GET("product/search")
    Call<Products> getProductsById(@Query("_id") String _id);


    //https://fastandfurious217.herokuapp.com/product/filter?name=samsung
    @GET("product/filter")
    Call<Products> getProductsByName(@Query("name") String name);


    @POST("bill")
    Call<Void> postBill(@Query("username") String username,
                       @Query("name") String name,
                       @Query("address") String address,
                       @Query("phone") String phone,
                       @Query("id_product") String id_product,
                       @Query("total") int total,
                       @Query("time") String time);

}
