package com.shoppingapp.common;

import android.app.Activity;
import android.app.Application;

import com.shoppingapp.di.AppComponent;
import com.shoppingapp.di.AppModule;
import com.shoppingapp.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class ShoppingApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    public static ShoppingApplication instance;

    @Override
    public void onCreate() {
        instance = this;
        setupTimber();
        getComponent().inject(this);
        super.onCreate();
    }

    private void setupTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    public AppComponent getComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(getApplicationContext()))
                .build();
    }

}
