package com.shoppingapp.common.base;

import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<T extends BaseView> implements Presenter<T> {

    public T view = null;

    public void setView(T view) {
        this.view = view;
    }

    public CompositeDisposable compositeDisposable=new CompositeDisposable();

    private boolean isViewAttached;

    public T getView() {
        if (view != null)
            isViewAttached = true;
        return view;
    }


    void checkViewAttached() {
        if (!isViewAttached) {
            Log.e("Error = ","Please call Presenter.attachView(BaseView) before " +
                    "requesting data to the Presenter");
        }
    }

    @Override
    public void attachView(T mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        view = null;
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
