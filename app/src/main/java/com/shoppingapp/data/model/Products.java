package com.shoppingapp.data.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

public class Products {

    private List<ProductsBean> products;

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    @Entity(tableName = "products")
    public static class ProductsBean {
        /**
         * name : OnePlus 6
         * price : 40000
         * image_url : https://i.gadgets360cdn.com/products/large/1526490365_635_oneplus_6.jpg
         * rating : 4.5
         */

        @PrimaryKey
        @NonNull
        @ColumnInfo(name = "name")
        private String name;
        @ColumnInfo(name = "price")
        private String price;
        @ColumnInfo(name = "image_url")
        private String image_url;
        @ColumnInfo(name = "rating")
        private double rating;
        @ColumnInfo(name = "isAddedToCart")
        private boolean isAddedToCart;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public boolean isAddedToCart() {
            return isAddedToCart;
        }

        public void setAddedToCart(boolean addedToCart) {
            isAddedToCart = addedToCart;
        }
    }
}
