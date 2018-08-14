package com.shoppingapp.shopping.cart;

import com.shoppingapp.common.base.BasePresenter;
import com.shoppingapp.data.ShoppingRepository;
import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CartPresenter extends BasePresenter<CartView> {

    private ShoppingRepository shoppingRepository;

    @Inject
    public CartPresenter(ShoppingRepository shoppingRepository) {
        this.shoppingRepository = shoppingRepository;
    }

    void getCartProducts() {
        if (view != null) {
            view.hideError();
            view.showLoader();
        }
        compositeDisposable.add(shoppingRepository.getCartProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Products.ProductsBean>>() {
                    @Override
                    public void accept(List<Products.ProductsBean> productsBeans) {
                        if (view != null) {
                            view.hideLoader();
                            view.showProducts(productsBeans);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Timber.e(throwable);
                        if (view != null) {
                            view.hideLoader();
                            view.showError();
                        }
                    }
                }));
    }

    void retry() {
        if (view != null) {
            view.retry();
        }
    }

    public void removeProductFromCart(final Products.ProductsBean productsBean) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                shoppingRepository.updateProduct(productsBean);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
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

    public void placeOrder(final ArrayList<Products.ProductsBean> mCartProductsArray) {
        final String orderId = String.valueOf(System.currentTimeMillis());
        compositeDisposable.add(Observable.fromIterable(mCartProductsArray)
                .map(new Function<Products.ProductsBean, OrderModel>() {
                    @Override
                    public OrderModel apply(Products.ProductsBean productsBean) {
                        OrderModel order = new OrderModel(productsBean);
                        order.setOrderId(orderId);
                        return order;
                    }
                }).subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<OrderModel>() {
                    @Override
                    public void onNext(OrderModel orderBean) {
                        shoppingRepository.insertOrder(orderBean);
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
                            removeAllProductsFromCartCheckout(mCartProductsArray);
                            view.completeOrder();
                        }
                    }
                }));
    }

    private void removeAllProductFromCart(final Products.ProductsBean productsBean) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                shoppingRepository.updateAllProducts(false, productsBean.getName());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
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

    private void removeAllProductsFromCartCheckout(ArrayList<Products.ProductsBean> mCartProductsArray) {
        for (Products.ProductsBean productsBean : mCartProductsArray) {
            removeAllProductFromCart(productsBean);
        }
    }
}
