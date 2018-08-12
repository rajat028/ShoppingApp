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
import com.shoppingapp.data.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rajatarora.com.shoppingapp.R;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.CartProductHolder> {

    private ArrayList<OrderModel> mProductsArray = new ArrayList<>();

    @NonNull
    @Override
    public CartProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new CartProductHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductHolder holder, int position) {
        holder.bind(mProductsArray.get(position));
    }

    @Override
    public int getItemCount() {
        return mProductsArray.size();
    }

    public void updateProducts(List<OrderModel> products) {
        this.mProductsArray.clear();
        this.mProductsArray.addAll(products);
        notifyDataSetChanged();
    }

    class CartProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgProduct)
        ImageView imgProduct;
        @BindView(R.id.txtProductName)
        TextView txtProductName;
        @BindView(R.id.txtProductPrice)
        TextView txtProductPrice;
        @BindView(R.id.txtProductRating)
        TextView txtProductRating;
        @BindView(R.id.imgAddToCart)
        ImageView imgAddToCart;

        Context mContext;

        CartProductHolder(View view, Context context) {
            super(view);
            mContext = context;
            ButterKnife.bind(this, view);
        }

        void bind(final OrderModel orderBean) {
            Glide.with(mContext).load(orderBean.getImage_url()).into(imgProduct);
            txtProductName.setText(orderBean.getName());
            txtProductPrice.setText(new StringBuilder().append("Rs ").append(orderBean.getPrice()));
            txtProductRating.setText(String.valueOf(orderBean.getRating()));
            imgAddToCart.setVisibility(View.GONE);
        }
    }
}
