package com.shoppingapp.shopping.cart;

import com.shoppingapp.common.base.BasePresenter;
import com.shoppingapp.data.LocalRepository;
import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CartPresenter extends BasePresenter<CartView> {

    @Inject
    LocalRepository localRepository;

    @Inject
    CartPresenter() {

    }

    void getCartProducts() {
        if (view != null) {
            view.hideError();
            view.showLoader();
        }
        compositeDisposable.add(localRepository.getCartProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(productsBeans -> {
                    if (view != null) {
                        view.hideLoader();
                        view.showProducts(productsBeans);
                    }
                }, throwable -> {
                    Timber.e(throwable);
                    if (view != null) {
                        view.hideLoader();
                        view.showError();
                    }
                }));
    }

    void retry() {
        if (view != null) {
            view.retry();
        }
    }

    public void removeProductFromCart(final Products.ProductsBean productsBean) {
        Completable.fromAction(() -> localRepository.updateProductStatus(productsBean))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {

                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) {
                            view.showRemoveSucessMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (view != null)
                            view.showRemoveErrorMessage(throwable.getLocalizedMessage());
                    }

                });
    }

    public void placeOrder(final List<Products.ProductsBean> productsBeanList) {
        final String orderId = String.valueOf(System.currentTimeMillis());
        compositeDisposable.add(Observable.fromIterable(productsBeanList)
                .map(productsBean -> OrderModel.create(orderId, productsBean))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<OrderModel>() {
                    @Override
                    public void onNext(OrderModel orderBean) {
                        localRepository.insertOrder(orderBean);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Timber.e(throwable);
                        if (view != null) {
                            view.hideLoader();
                            view.showError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) {
                            removeAllProductsFromCartOnCheckout(productsBeanList);
                            view.completeOrder();
                        }
                    }
                }));
    }

    private void removeAllProductFromCart(final Products.ProductsBean productsBean) {
        Completable.fromAction(() -> localRepository.updateAllProducts(false, productsBean.getName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        compositeDisposable.add(disposable);
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) {
                            view.hideLoader();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (view != null)
                            view.showRemoveErrorMessage(throwable.getLocalizedMessage());
                    }
                });

    }

    private void removeAllProductsFromCartOnCheckout(List<Products.ProductsBean> productsBeanList) {
        for (Products.ProductsBean productsBean : productsBeanList) {
            removeAllProductFromCart(productsBean);
        }
    }
}
