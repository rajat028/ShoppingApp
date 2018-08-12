package com.shoppingapp.data.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface ProductDAO {

    @Query("SELECT * FROM products")
    Flowable<List<Products.ProductsBean>> getAllProducts();

    @Query("SELECT * FROM products where isAddedToCart")
    Flowable<List<Products.ProductsBean>> getCartProducts();

    @Query("SELECT order_id FROM `order`")
    Flowable<List<String>> getAllOrder();

    @Query("SELECT * FROM `order` WHERE order_id=:orderId")
    Flowable<List<OrderModel>> getProductByOrderId(String orderId);

    @Insert
    void insertProduct(Products.ProductsBean productsBean);

    @Update
    void updateProduct(Products.ProductsBean productsBean);

    @Query("UPDATE products SET isAddedToCart=:status WHERE name = :name")
    void updateAllProducts(boolean status, String name);

    @Insert
    void insertOrder(OrderModel oderBean);

}
