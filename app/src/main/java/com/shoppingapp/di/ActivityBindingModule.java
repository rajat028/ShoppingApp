package com.shoppingapp.di;

import com.shoppingapp.shopping.cart.CartActivity;
import com.shoppingapp.shopping.cart.di.CartActivityModule;
import com.shoppingapp.shopping.cart.di.CartScope;
import com.shoppingapp.shopping.orderDetail.OrderDetailActivity;
import com.shoppingapp.shopping.orderDetail.di.OrderDetailModule;
import com.shoppingapp.shopping.orderDetail.di.OrderDetailScope;
import com.shoppingapp.shopping.orderListing.OrderListingActivity;
import com.shoppingapp.shopping.orderListing.di.OrderListingModule;
import com.shoppingapp.shopping.orderListing.di.OrderListingScope;
import com.shoppingapp.shopping.products.ProductActivity;
import com.shoppingapp.shopping.products.di.ProductActivityModule;
import com.shoppingapp.shopping.products.di.ProductScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ActivityBindingModule {

    @ProductScope
    @ContributesAndroidInjector(modules = {ProductActivityModule.class})
    abstract ProductActivity bindProductActivity();

    @CartScope
    @ContributesAndroidInjector(modules = {CartActivityModule.class})
    abstract CartActivity bindCartActivity();

    @OrderListingScope
    @ContributesAndroidInjector(modules = {OrderListingModule.class})
    abstract OrderListingActivity bindOrderListingActivity();

    @OrderDetailScope
    @ContributesAndroidInjector(modules = {OrderDetailModule.class})
    abstract OrderDetailActivity bindOrderDetailActivity();
}
