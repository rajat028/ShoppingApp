package com.shoppingapp.data.remote;

import com.shoppingapp.data.model.Products;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ShoppingApi {

    @GET("bins/i5qto")
    Observable<Products> getProducts();
}
