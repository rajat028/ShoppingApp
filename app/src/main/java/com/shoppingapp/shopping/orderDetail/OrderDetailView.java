package com.shoppingapp.shopping.orderDetail;

import com.shoppingapp.common.base.BaseView;
import com.shoppingapp.data.model.OrderModel;

import java.util.List;

public interface OrderDetailView extends BaseView {
    void showLoader();

    void hideLoader();

    void showError();

    void hideError();

    void retry();

    void showProducts(List<OrderModel> productsBeans);
}
