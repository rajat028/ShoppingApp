package com.shoppingapp.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public abstract class BaseActivity<P extends Presenter<?>> extends AppCompatActivity {

    @Inject
    public P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(layout());
        ButterKnife.bind(this);
        attachView();
        initView();
    }

    protected abstract int layout();

    protected abstract void initView();

    protected abstract void attachView();

    private void detachView() {
        presenter.detachView();
    }

    @Override
    protected void onDestroy() {
        detachView();
        super.onDestroy();
    }
}
