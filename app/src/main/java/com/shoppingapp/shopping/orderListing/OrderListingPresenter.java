package com.shoppingapp.shopping.orderListing;

import com.shoppingapp.common.base.BasePresenter;
import com.shoppingapp.data.LocalRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class OrderListingPresenter extends BasePresenter<OrderListingView> {

    private LocalRepository localRepository;

    @Inject
    public OrderListingPresenter(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    void getAllOrders() {

        if (view != null) {
            view.hideError();
            view.showLoader();
        }
        compositeDisposable.add(localRepository.getAllOrder()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(orderBeans -> {
                    if (view != null) {
                        view.hideLoader();
                        view.showOrders(orderBeans);
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
}
