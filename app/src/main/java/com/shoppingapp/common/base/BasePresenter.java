package com.shoppingapp.common.base;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the view that
 * can be accessed from the children classes by calling getView().
 */
public class BasePresenter<T extends BaseView> implements Presenter<T> {

    public T view = null;

    public void setView(T view) {
        this.view = view;
    }

    public CompositeDisposable compositeDisposable = new CompositeDisposable();

    public T getView() {
        return view;
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
