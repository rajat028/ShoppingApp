package com.shoppingapp.shopping.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shoppingapp.common.base.BaseActivity;
import com.shoppingapp.data.model.Products;
import com.shoppingapp.exts.ItemOffsetDecoration;
import com.shoppingapp.shopping.cart.adapter.CartAdapter;
import com.shoppingapp.shopping.orderListing.OrderListingActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rajatarora.com.shoppingapp.R;

public class CartActivity extends BaseActivity<CartPresenter> implements CartView, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.llError)
    LinearLayout llError;

    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;

    @BindView(R.id.btnRetry)
    Button btnRetry;

    @BindView(R.id.pbLoader)
    ProgressBar pbLoader;

    @BindView(R.id.txtNoProducts)
    TextView txtNoProducts;

    @BindView(R.id.llCartDetails)
    LinearLayout llCartDetails;

    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;

    @BindView(R.id.btnPlaceOrder)
    Button btnPlaceOrder;

    private ArrayList<Products.ProductsBean> mCartProductsArray = new ArrayList<>();

    @Inject
    CartAdapter mCartAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, CartActivity.class);
    }

    @Override
    protected int layout() {
        return R.layout.activity_cart;
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.cart);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        setupRecyclerView();
        initListeners();
    }

    private void initListeners() {
        btnPlaceOrder.setOnClickListener(this);
        btnRetry.setOnClickListener(this);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen._1sdp);
        rvProducts.addItemDecoration(itemDecoration);
        rvProducts.setAdapter(mCartAdapter);
        fetchProducts();
    }

    private void fetchProducts() {
        presenter.getCartProducts();
    }

    @Override
    protected void attachView() {
        presenter.attachView(this);
    }

    @Override
    public void showLoader() {
        pbLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        pbLoader.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        llError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        llError.setVisibility(View.GONE);
    }

    @Override
    public void retry() {
        presenter.getCartProducts();
    }

    @Override
    public void showProducts(List<Products.ProductsBean> productData) {
        mCartProductsArray.clear();
        mCartProductsArray.addAll(productData);
        mCartAdapter.updateProducts(mCartProductsArray);
        showTotalCartPrice(productData);
        rvProducts.setVisibility(View.VISIBLE);
    }

    public void showTotalCartPrice(List<Products.ProductsBean> productData) {
        Integer mTotalPrice = 0;
        for (Products.ProductsBean productValues : productData) {
            mTotalPrice = mTotalPrice + Integer.parseInt(productValues.getPrice());
        }
        tvTotalPrice.setText(new StringBuilder().append("Rs ").append(mTotalPrice));

        if (mTotalPrice == 0) {
            txtNoProducts.setVisibility(View.VISIBLE);
            btnPlaceOrder.setEnabled(false);
        } else {
            txtNoProducts.setVisibility(View.GONE);
            btnPlaceOrder.setEnabled(true);
        }
    }

    @Override
    public void removeFromCart(Products.ProductsBean productsBean) {
        alertRemoveConfirmation(productsBean);
    }

    @Override
    public void showRemoveErrorMessage(String localizedMessage) {
        Toast.makeText(this, localizedMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRemoveSucessMessage() {
        Toast.makeText(this, R.string.removed_cart_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void completeOrder() {
        navigateToOrderListing();
    }

    private void navigateToOrderListing() {
        startActivity(new Intent(this, OrderListingActivity.class));
        finish();
        navigateToNext();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlaceOrder:
                alertPlaceOrder();
                break;
            case R.id.btnRetry:
                presenter.retry();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                navigateToPrevious();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void alertRemoveConfirmation(final Products.ProductsBean productsBean) {
        showDialog(getString(R.string.remove_product),
                getString(R.string.remove_product_confirmation),
                new RemoveProductListener(productsBean));
    }


    private void alertPlaceOrder() {
        showDialog(getString(R.string.confrim_order),
                getString(R.string.confirm_buy_message),
                new ConfirmBuyListener());
    }

    private void showDialog(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setPositiveButton(R.string.confirm, listener);
        alertDialog.show();

    }

    class RemoveProductListener implements DialogInterface.OnClickListener {
        Products.ProductsBean productsBean;

        RemoveProductListener(Products.ProductsBean productsBean) {
            this.productsBean = productsBean;
        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            presenter.removeProductFromCart(productsBean);
        }
    }

    class ConfirmBuyListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            presenter.placeOrder(mCartProductsArray);
        }
    }

}
