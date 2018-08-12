package com.shoppingapp.common.base;

public interface Presenter<V extends BaseView> {
    void attachView(V mvpView);
    void detachView();
}
