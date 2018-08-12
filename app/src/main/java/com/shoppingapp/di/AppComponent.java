package com.shoppingapp.di;

import com.shoppingapp.common.ShoppingApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AppModule.class,
        ApiModule.class,
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBindingModule.class})

public interface AppComponent {
    void inject(ShoppingApplication application);
}
