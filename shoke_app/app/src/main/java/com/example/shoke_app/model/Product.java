package com.example.shoke_app.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String _id;
    private int id;
    private String name;
    private String description;
    private float price;
    private String img;
    private int sortOut;
    private int total;
    private String producer;

    public Product(String _id, int id, String name, String description, float price, String img, int sortOut, int total, String producer) {
        this._id = _id;
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.img = img;
        this.sortOut = sortOut;
        this.total = total;
        this.producer = producer;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getSortOut() {
        return sortOut;
    }

    public void setSortOut(int sortOut) {
        this.sortOut = sortOut;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }
}
