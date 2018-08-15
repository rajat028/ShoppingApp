package com.shoppingapp.di;


import com.shoppingapp.data.remote.ShoppingApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = TestNetworkModule.class)
public class TestApiModule {

    @Provides
    @Singleton
    public ShoppingApi getShoppingApi(Retrofit retrofit) {
        return retrofit.create(ShoppingApi.class);
    }
}
