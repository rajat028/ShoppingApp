package com.shoppingapp;

import com.shoppingapp.common.ShoppingApplication;
import com.shoppingapp.di.AppComponent;
import com.shoppingapp.di.DaggerAppComponent;
import com.shoppingapp.di.DaggerTestAppComponent;

public class TestApplication extends ShoppingApplication {
    @Override
    public AppComponent getComponent() {
        return DaggerTestAppComponent.builder()
                .build();
    }
}
