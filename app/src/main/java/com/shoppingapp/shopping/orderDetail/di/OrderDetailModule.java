package com.shoppingapp.shopping.orderDetail.di;

import com.shoppingapp.shopping.orderDetail.adapter.OrderDetailAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class OrderDetailModule {

    @OrderDetailScope
    @Provides
    public OrderDetailAdapter getOrderDetailAdapter() {
        return new OrderDetailAdapter();
    }
}
