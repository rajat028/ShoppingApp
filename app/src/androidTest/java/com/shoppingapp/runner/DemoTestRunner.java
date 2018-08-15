package com.shoppingapp.runner;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import com.shoppingapp.TestApplication;

import io.appflate.restmock.RESTMockServerStarter;
import io.appflate.restmock.android.AndroidAssetsFileParser;
import io.appflate.restmock.android.AndroidLogger;

public class DemoTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        RESTMockServerStarter.startSync(new AndroidAssetsFileParser(getContext()), new AndroidLogger());
        return super.newApplication(cl, TestApplication.class.getName(), context);
    }
}
