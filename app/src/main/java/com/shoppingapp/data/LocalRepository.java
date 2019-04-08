package com.shoppingapp.data;

import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface LocalRepository {

    Flowable<List<Products.ProductsBean>> getAllProducts();

    Flowable<List<Products.ProductsBean>> getCartProducts();

    Single<List<String>> getAllOrder();

    Single<List<OrderModel>> getProductByOrderId(String orderId);

    Completable insertProduct(Products.ProductsBean productsBean);

    Completable updateProductStatus(Products.ProductsBean productsBean);

    Completable updateAllProducts(boolean status, String name);

    Completable insertOrder(OrderModel oderBean);
}
