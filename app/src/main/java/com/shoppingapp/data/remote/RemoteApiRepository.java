package com.shoppingapp.data.remote;

import com.shoppingapp.data.RemoteRepository;
import com.shoppingapp.data.model.Products;

import io.reactivex.Observable;

public class RemoteApiRepository implements RemoteRepository {

    private ShoppingApi shoppingApi;

    public RemoteApiRepository(ShoppingApi shoppingApi) {
        this.shoppingApi = shoppingApi;
    }

    @Override
    public Observable<Products> getProducts() {
        return shoppingApi.getProducts();
    }
}
