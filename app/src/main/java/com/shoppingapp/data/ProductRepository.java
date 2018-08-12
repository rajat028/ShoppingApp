package com.shoppingapp.data;

import com.shoppingapp.data.model.Products;

import io.reactivex.Observable;

public interface ProductRepository  {

    Observable<Products> getProducts();

}
