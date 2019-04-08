package com.shoppingapp.shopping.products.adapter;

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
import com.shoppingapp.data.model.Products;
import com.shoppingapp.shopping.products.ProductActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder> {

    private List<Products.ProductsBean> productsBeanList = new ArrayList<>();

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ProductHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.bind(productsBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return productsBeanList.size();
    }

    public void setProducts(List<Products.ProductsBean> products) {
        this.productsBeanList.clear();
        this.productsBeanList.addAll(products);
        notifyDataSetChanged();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

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

        ProductHolder(View view, Context context) {
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

            itemView.setOnClickListener(view -> {
                productsBean.setAddedToCart(true);
                ((ProductActivity) context).addToCart(productsBean);
            });
        }
    }
}
