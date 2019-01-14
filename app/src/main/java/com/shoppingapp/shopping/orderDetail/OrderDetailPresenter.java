package com.shoppingapp.shopping.orderDetail;

import com.shoppingapp.common.base.BasePresenter;
import com.shoppingapp.data.LocalRepository;
import com.shoppingapp.data.model.OrderModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
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
                .subscribe(new Consumer<List<OrderModel>>() {
                    @Override
                    public void accept(List<OrderModel> orderBean) {
                        if (view != null) {
                            view.hideLoader();
                            view.showProducts(orderBean);
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
