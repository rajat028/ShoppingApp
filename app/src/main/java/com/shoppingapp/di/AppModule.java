package com.shoppingapp.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.shoppingapp.data.ShoppingRepository;
import com.shoppingapp.data.Local.DatabaseRepository;
import com.shoppingapp.data.Local.ProductDAO;
import com.shoppingapp.data.Local.ShoppingDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context application;

    public AppModule(Context application) {
        this.application = application;
    }


    @Singleton
    @Provides
    public ShoppingDatabase getShoppingDatabase() {
        return Room.databaseBuilder(application,ShoppingDatabase.class,"shopping_local")
                .build();
    }

    @Singleton
    @Provides
    public ProductDAO getProductDAO(ShoppingDatabase shoppingDatabase) {
        return shoppingDatabase.productDAO();
    }

    @Singleton
    @Provides
    public ShoppingRepository getShoppingRepository(ProductDAO productDAO) {
        return new DatabaseRepository(productDAO);
    }



}
