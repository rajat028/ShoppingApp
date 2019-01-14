package com.shoppingapp.data.Local;

import com.shoppingapp.data.LocalRepository;
import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;

import java.util.List;

import io.reactivex.Flowable;

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
    public Flowable<List<String>> getAllOrder() {
        return productDAO.getAllOrder();
    }

    @Override
    public Flowable<List<OrderModel>> getProductByOrderId(String orderId) {
        return productDAO.getProductByOrderId(orderId);
    }

    @Override
    public void insertProduct(Products.ProductsBean productsBean) {
        productDAO.insertProduct(productsBean);
    }

    @Override
    public void updateProductStatus(Products.ProductsBean productsBean) {
        productDAO.updateProductStatus(productsBean);
    }

    @Override
    public void updateAllProducts(boolean status, String name) {
        productDAO.updateAllProducts(status, name);
    }

    @Override
    public void insertOrder(OrderModel oderBean) {
        productDAO.insertOrder(oderBean);
    }
}
