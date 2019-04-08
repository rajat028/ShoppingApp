package com.shoppingapp.data.remote;

import com.shoppingapp.data.model.Products;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ShoppingApi {

    @GET("bins/i5qto")
    Single<Products> getProducts();
}
