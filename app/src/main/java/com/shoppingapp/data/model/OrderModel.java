package com.shoppingapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "order")
public class OrderModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "order_id")
    private String orderId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "price")
    private String price;
    @ColumnInfo(name = "image_url")
    private String image_url;
    @ColumnInfo(name = "rating")
    private double rating;

    public OrderModel() {

    }

    public OrderModel(int id, String orderId, String name, String price, String image_url, double rating) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.image_url = image_url;
        this.rating = rating;
    }

    public OrderModel(String orderId, Products.ProductsBean productsBean) {
        this.orderId = orderId;
        name = productsBean.getName();
        price = productsBean.getPrice();
        image_url = productsBean.getImage_url();
        rating = productsBean.getRating();
    }

    public static OrderModel create(String orderId, Products.ProductsBean productsBean) {
        return new OrderModel(orderId, productsBean);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(@NonNull String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


}
