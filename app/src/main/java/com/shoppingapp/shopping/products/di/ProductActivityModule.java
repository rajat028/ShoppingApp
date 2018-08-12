package com.shoppingapp.shopping.products.di;

import com.shoppingapp.data.ProductRepository;
import com.shoppingapp.data.remote.ProductApiRepository;
import com.shoppingapp.data.remote.ShoppingApi;
import com.shoppingapp.shopping.products.adapter.ProductsAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ProductActivityModule {

    @Provides
    @ProductScope
    public ProductsAdapter getProductAdapter() {
        return new ProductsAdapter();
    }

    @Provides
    @ProductScope
    public ProductRepository getProductRepository(ShoppingApi shoppingApi) {
        return new ProductApiRepository(shoppingApi);
    }

}
