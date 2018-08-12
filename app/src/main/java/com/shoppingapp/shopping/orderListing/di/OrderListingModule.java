package com.shoppingapp.shopping.orderListing.di;

import com.shoppingapp.shopping.orderListing.adapter.OrderListingAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class OrderListingModule {

    @Provides
    @OrderListingScope
    public OrderListingAdapter getOrderListingAdapter() {
        return new OrderListingAdapter();
    }

}
