package com.shoppingapp.shopping.cart.di;

import com.shoppingapp.shopping.cart.adapter.CartAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class CartActivityModule {

    @Provides
    @CartScope
    public CartAdapter getCartAdapter() {
        return new CartAdapter();
    }

}
