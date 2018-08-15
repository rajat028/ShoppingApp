package com.shoppingapp.shopping.orderDetail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shoppingapp.R;
import com.shoppingapp.data.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.CartProductHolder> {

    private List<OrderModel> orderedProductsList = new ArrayList<>();

    @NonNull
    @Override
    public CartProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new CartProductHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductHolder holder, int position) {
        holder.bind(orderedProductsList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderedProductsList.size();
    }

    public void updateProducts(List<OrderModel> products) {
        this.orderedProductsList.clear();
        this.orderedProductsList.addAll(products);
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

        void bind(final OrderModel orderBean) {
            Glide.with(context).load(orderBean.getImage_url()).into(ivProduct);
            tvProductName.setText(orderBean.getName());
            tvProductPrice.setText(new StringBuilder().append("Rs ").append(orderBean.getPrice()));
            tvProductRating.setText(String.valueOf(orderBean.getRating()));
            ivAddToCart.setVisibility(View.GONE);
        }
    }
}
