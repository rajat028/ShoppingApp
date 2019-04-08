package com.shoppingapp.shopping.products;

import com.shoppingapp.common.base.BasePresenter;
import com.shoppingapp.data.LocalRepository;
import com.shoppingapp.data.ProductsRepository;
import com.shoppingapp.data.model.Products;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ProductPresenter extends BasePresenter<ProductView> {

    // Repository to handle api operations.
    private ProductsRepository productRepository;

    // Repository to handle database operations.
    private LocalRepository localRepository;

    @Inject
    public ProductPresenter(ProductsRepository productsRepository, LocalRepository localRepository) {
        this.productRepository = productsRepository;
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
                .subscribe(productsBeans -> {
                    if (view != null) {
                        if (productsBeans.isEmpty()) {
                            fetchProductsFromAPI();
                        } else {
                            view.hideLoader();
                            view.showProducts(productsBeans);
                        }
                    }
                }, throwable -> {
                    Timber.e(throwable);
                    if (view != null) {
                        view.hideLoader();
                        view.showError();
                    }
                }));
    }

    void fetchProductsFromAPI() {
        compositeDisposable.add(productRepository.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(products -> {
                    if (view != null) {
                        insertToLocal(products.getProducts());
                        view.hideLoader();
                        view.showProducts(products.getProducts());
                    }
                }, throwable -> {
                    Timber.e(throwable);
                    if (view != null) {
                        view.hideLoader();
                        view.showError();
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

        localRepository.updateProductStatus(productsBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Timber.d("XXXX Update complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("XXXX Update error");
                    }
                });

//        Completable.fromAction(() ->
//                localRepository.updateProductStatus(productsBean))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new CompletableObserver() {
//                    @Override
//                    public void onSubscribe(Disposable disposable) {
//                        compositeDisposable.add(disposable);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        if (view != null)
//                            view.showAddToCartSucess();
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        if (view != null)
//                            view.showAddToCartError(throwable.getLocalizedMessage());
//                    }
//                });
    }

    void retry() {
        if (view != null) {
            view.retry();
        }
    }
}
