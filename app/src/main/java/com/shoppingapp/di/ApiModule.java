package com.shoppingapp.di;

import com.shoppingapp.data.remote.ShoppingApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = {NetworkModule.class})
public class ApiModule {
    @Provides
    @Singleton
    public ShoppingApi shoppingApi(Retrofit retrofit) {
        return retrofit.create(ShoppingApi.class);
    }



}
