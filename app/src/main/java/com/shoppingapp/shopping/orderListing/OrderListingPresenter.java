package com.shoppingapp.shopping.orderListing;

import com.shoppingapp.common.base.BasePresenter;
import com.shoppingapp.data.LocalRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> orderBeans) {
                        if (view != null) {
                            view.hideLoader();
                            view.showOrders(orderBeans);
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
}
