package com.shoppingapp.data.remote;

import com.shoppingapp.data.ProductsRepository;
import com.shoppingapp.data.model.Products;

import io.reactivex.Single;

public class

ProductApiRepository implements ProductsRepository {

    private ShoppingApi shoppingApi;

    public ProductApiRepository(ShoppingApi shoppingApi) {
        this.shoppingApi = shoppingApi;
    }

    @Override
    public Single<Products> getProducts() {
        return shoppingApi.getProducts();
    }
}
