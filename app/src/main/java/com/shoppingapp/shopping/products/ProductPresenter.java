package com.shoppingapp.shopping.products;

import com.shoppingapp.common.base.BasePresenter;
import com.shoppingapp.data.LocalRepository;
import com.shoppingapp.data.RemoteRepository;
import com.shoppingapp.data.model.Products;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ProductPresenter extends BasePresenter<ProductView> {

    // Repository to handle api operations.
    private RemoteRepository productRepository;

    // Repository to handle database operations.
    private LocalRepository localRepository;

    @Inject
    public ProductPresenter(RemoteRepository productRepository, LocalRepository localRepository) {
        this.productRepository = productRepository;
        this.localRepository = localRepository;
    }

    void getProducts() {
        if (view != null) {
            view.hideError();
            view.showLoader();
        }
        compositeDisposable.add(localRepository.getAllProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Products.ProductsBean>>() {
                    @Override
                    public void accept(List<Products.ProductsBean> productsBeans) {
                        if (view != null) {
                            if (productsBeans.isEmpty()) {
                                fetchProductsFromAPI();
                            } else {
                                view.hideLoader();
                                view.showProducts(productsBeans);
                            }
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

    void fetchProductsFromAPI() {
        compositeDisposable.add(productRepository.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Products>() {
                    @Override
                    public void onNext(Products value) {
                        if (view != null) {
                            insertToLocal(value.getProducts());
                            view.hideLoader();
                            view.showProducts(value.getProducts());
                        }
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
                        /// no op
                    }
                }));
    }

    void insertToLocal(final List<Products.ProductsBean> productsBeanList) {
        compositeDisposable.add(Observable.fromIterable(productsBeanList)
                .subscribeOn(Schedulers.io())
                .subscribeWith(new DisposableObserver<Products.ProductsBean>() {
                    @Override
                    public void onNext(Products.ProductsBean productsBean) {
                        localRepository.insertProduct(productsBean);
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
                        // no op
                    }
                }));
    }

    void addProductToCart(final Products.ProductsBean productsBean) {
        Completable.fromAction(new Action() {
            @Override
            public void run() {
                localRepository.updateProductStatus(productsBean);
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
                        if (view != null)
                            view.showAddToCartSucess();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (view != null)
                            view.showAddToCartError(throwable.getLocalizedMessage());
                    }
                });
    }

    void retry() {
        if (view != null) {
            view.retry();
        }
    }
}
