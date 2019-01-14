package com.shoppingapp.data.remote;

import com.shoppingapp.data.RemoteRepository;
import com.shoppingapp.data.model.Products;

import io.reactivex.Observable;

public class

ProductApiRepository implements RemoteRepository {

    private ShoppingApi shoppingApi;

    public ProductApiRepository(ShoppingApi shoppingApi) {
        this.shoppingApi = shoppingApi;
    }

    @Override
    public Observable<Products> getProducts() {
        return shoppingApi.getProducts();
    }
}
