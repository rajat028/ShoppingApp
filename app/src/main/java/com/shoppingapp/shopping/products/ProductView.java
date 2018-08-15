package com.shoppingapp.shopping.products;

import com.shoppingapp.common.base.BaseView;
import com.shoppingapp.data.model.Products;

import java.util.List;

public interface ProductView extends BaseView {

    void showLoader();

    void hideLoader();

    void showError();

    void hideError();

    void retry();

    void showProducts(List<Products.ProductsBean> productData);

    void addToCart(Products.ProductsBean productsBean);

    void showAddToCartError(String localizedMessage);

    void showAddToCartSucess();
}
