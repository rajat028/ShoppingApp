package com.shoppingapp.shopping.cart;

import com.shoppingapp.common.base.BaseView;
import com.shoppingapp.data.model.Products;

import java.util.List;

public interface CartView extends BaseView{

    void showLoader();
    void hideLoader();
    void showError();
    void hideError();
    void retry();
    void showProducts(List<Products.ProductsBean> productData);
    void removeFromCart(Products.ProductsBean productsBean);
    void showRemoveErrorMessage(String localizedMessage);
    void showRemoveSucessMessage();
    void completeOrder();

}
