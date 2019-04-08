package com.shoppingapp.data;

import com.shoppingapp.data.model.Products;

import io.reactivex.Single;

public interface ProductsRepository {

    Single<Products> getProducts();
}
