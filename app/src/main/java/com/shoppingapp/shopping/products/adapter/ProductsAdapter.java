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
import com.shoppingapp.data.model.Products;
import com.shoppingapp.shopping.products.ProductActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rajatarora.com.shoppingapp.R;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder> {

    private ArrayList<Products.ProductsBean> mProductsArray = new ArrayList<>();

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);

        return new ProductHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.bind(mProductsArray.get(position));
    }

    @Override
    public int getItemCount() {
        return mProductsArray.size();
    }

    public void setProducts(List<Products.ProductsBean> products) {
        this.mProductsArray.clear();
        this.mProductsArray.addAll(products);
        notifyDataSetChanged();
    }

    class ProductHolder extends RecyclerView.ViewHolder {

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

        ProductHolder(View view, Context context) {
            super(view);
            mContext = context;
            ButterKnife.bind(this, view);
        }

        void bind(final Products.ProductsBean productsBean) {
            Glide.with(mContext).load(productsBean.getImage_url()).into(imgProduct);
            txtProductName.setText(productsBean.getName());
            txtProductPrice.setText(new StringBuilder().append("Rs ").append(productsBean.getPrice()));
            txtProductRating.setText(String.valueOf(productsBean.getRating()));

            if (productsBean.isAddedToCart())
                imgAddToCart.setImageResource(R.mipmap.ic_action_bookmark);
            else
                imgAddToCart.setImageResource(R.mipmap.ic_action_bookmark_border);

            imgAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productsBean.setAddedToCart(true);
                    ((ProductActivity) mContext).addToCart(productsBean);
                }
            });

        }
    }

}
