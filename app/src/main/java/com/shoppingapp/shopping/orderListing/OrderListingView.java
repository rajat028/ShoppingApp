package com.shoppingapp.shopping.orderListing;

import com.shoppingapp.common.base.BaseView;

import java.util.List;

public interface OrderListingView extends BaseView {
    void showLoader();
    void hideLoader();
    void showError();
    void hideError();
    void retry();
    void showOrders(List<String> orderArray);
    void viewOrderDetail(String orderId);
}
