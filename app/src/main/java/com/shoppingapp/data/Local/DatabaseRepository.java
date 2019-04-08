package com.shoppingapp.data.Local;

import com.shoppingapp.data.LocalRepository;
import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class DatabaseRepository implements LocalRepository {

    private ProductDAO productDAO;

    public DatabaseRepository(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Flowable<List<Products.ProductsBean>> getAllProducts() {
        return productDAO.getAllProducts();
    }

    @Override
    public Flowable<List<Products.ProductsBean>> getCartProducts() {
        return productDAO.getCartProducts();
    }

    @Override
    public Single<List<String>> getAllOrder() {
        return productDAO.getAllOrder();
    }

    @Override
    public Single<List<OrderModel>> getProductByOrderId(String orderId) {
        return productDAO.getProductByOrderId(orderId);
    }

    @Override
    public Completable insertProduct(Products.ProductsBean productsBean) {
        return Completable.fromAction(() ->
                productDAO.insertProduct(productsBean));
    }

    @Override
    public Completable updateProductStatus(Products.ProductsBean productsBean) {
        return Completable.fromAction(() -> productDAO.updateProductStatus(productsBean));
    }

    private void runProcess() {

    }

    @Override
    public Completable updateAllProducts(boolean status, String name) {
        return Completable.fromAction(() ->
                productDAO.updateAllProducts(status, name));
    }

    @Override
    public Completable insertOrder(OrderModel oderBean) {
        return Completable.fromAction(() ->
                productDAO.insertOrder(oderBean));
    }
}
