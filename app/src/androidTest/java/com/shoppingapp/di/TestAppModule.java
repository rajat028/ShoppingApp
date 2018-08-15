package com.shoppingapp.di;

import android.app.Application;

import dagger.Module;

@Module
public class TestAppModule extends AppModule {

    public TestAppModule(Application application) {
        super(application);
    }
}
