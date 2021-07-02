package com.example.shoke_app.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Products implements Serializable {
    private boolean success;
    private ArrayList<Product> products;

    public Products(boolean success, ArrayList<Product> products) {
        this.success = success;
        this.products = products;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}