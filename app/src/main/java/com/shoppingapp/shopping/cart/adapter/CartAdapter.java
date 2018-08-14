package com.shoppingapp.shopping.cart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shoppingapp.data.model.Products;
import com.shoppingapp.shopping.cart.CartActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rajatarora.com.shoppingapp.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartProductHolder> {

    private ArrayList<Products.ProductsBean> productsBeanArrayList = new ArrayList<>();

    @NonNull
    @Override
    public CartProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new CartProductHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductHolder holder, int position) {
        holder.bind(productsBeanArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return productsBeanArrayList.size();
    }

    public void updateProducts(List<Products.ProductsBean> products) {
        this.productsBeanArrayList.clear();
        this.productsBeanArrayList.addAll(products);
        notifyDataSetChanged();
    }

    class CartProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProduct)
        ImageView ivProduct;
        @BindView(R.id.tvProductName)
        TextView tvProductName;
        @BindView(R.id.tvProductPrice)
        TextView tvProductPrice;
        @BindView(R.id.tvProductRating)
        TextView tvProductRating;
        @BindView(R.id.ivAddToCart)
        ImageView ivAddToCart;

        Context context;

        CartProductHolder(View view, Context context) {
            super(view);
            this.context = context;
            ButterKnife.bind(this, view);
        }

        void bind(final Products.ProductsBean productsBean) {
            Glide.with(context).load(productsBean.getImage_url()).into(ivProduct);
            tvProductName.setText(productsBean.getName());
            tvProductPrice.setText(new StringBuilder().append("Rs ").append(productsBean.getPrice()));
            tvProductRating.setText(String.valueOf(productsBean.getRating()));

            if (productsBean.isAddedToCart())
                ivAddToCart.setImageResource(R.mipmap.ic_action_bookmark);
            else
                ivAddToCart.setImageResource(R.mipmap.ic_action_bookmark_border);

            ivAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productsBean.setAddedToCart(false);
                    ((CartActivity) context).removeFromCart(productsBean);
                }
            });

        }
    }
}
