package com.shoppingapp.data;

import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;

import java.util.List;

import io.reactivex.Flowable;

public interface ShoppingRepository  {

    Flowable<List<Products.ProductsBean>> getAllProducts();

    Flowable<List<Products.ProductsBean>> getCartProducts();

    Flowable<List<String>> getAllOrder();

    Flowable<List<OrderModel>> getProductByOrderId(String orderId);

    void insertProduct(Products.ProductsBean productsBean);

    void updateProductStatus(Products.ProductsBean productsBean);

    void updateAllProducts(boolean status, String name);

    void insertOrder(OrderModel oderBean);
}
