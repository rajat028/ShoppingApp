package com.shoppingapp.shopping.orderListing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoppingapp.R;
import com.shoppingapp.shopping.orderListing.OrderListingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderListingAdapter extends RecyclerView.Adapter<OrderListingAdapter.CartProductHolder> {

    private List<String> ordersList = new ArrayList<>();

    @NonNull
    @Override
    public CartProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_orders, parent, false);
        return new CartProductHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductHolder holder, int position) {
        holder.bind(ordersList.get(position));
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public void updateOrders(List<String> orders) {
        this.ordersList.clear();
        this.ordersList.addAll(orders);
        notifyDataSetChanged();
    }

    class CartProductHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cvClick)
        CardView cvClick;
        @BindView(R.id.tvOrderId)
        TextView tvOrderId;

        Context context;

        CartProductHolder(View view, Context context) {
            super(view);
            this.context = context;
            ButterKnife.bind(this, view);
        }

        void bind(final String orderId) {
            tvOrderId.setText(orderId);
            cvClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OrderListingActivity) context).viewOrderDetail(orderId);
                }
            });
        }
    }
}
