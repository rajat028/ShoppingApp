package com.shoppingapp.shopping.orderDetail;

import com.shoppingapp.common.base.BasePresenter;
import com.shoppingapp.data.LocalRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class OrderDetailPresenter extends BasePresenter<OrderDetailView> {

    private LocalRepository localRepository;

    @Inject
    public OrderDetailPresenter(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public void getOrderedProductById(String mOrderId) {
        if (view != null) {
            view.hideError();
            view.showLoader();
        }
        compositeDisposable.add(localRepository.getProductByOrderId(mOrderId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(orderBean -> {
                    if (view != null) {
                        view.hideLoader();
                        view.showProducts(orderBean);
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
