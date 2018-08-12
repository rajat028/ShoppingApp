package com.shoppingapp.data.Local;

import android.arch.persistence.room.RoomDatabase;

import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;


@android.arch.persistence.room.Database(entities = {Products.ProductsBean.class, OrderModel.class}, version = 1)
public abstract class ShoppingDatabase extends RoomDatabase{
    public abstract ProductDAO productDAO();
}
