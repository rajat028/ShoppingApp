package com.shoppingapp.di;

import dagger.Module;
import io.appflate.restmock.RESTMockServer;

@Module
public class TestNetworkModule extends NetworkModule {

    @Override
    public String getBaseUrl() {
        return RESTMockServer.getUrl();
    }
}
