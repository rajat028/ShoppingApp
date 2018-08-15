package com.shoppingapp.di;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        TestAppModule.class,
        TestApiModule.class,
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        TestActivityBindingModule.class})
interface TestAppComponent extends AppComponent {

}
