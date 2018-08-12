package com.shoppingapp.shopping.products;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shoppingapp.common.base.BaseActivity;
import com.shoppingapp.data.model.Products;
import com.shoppingapp.exts.ItemOffsetDecoration;
import com.shoppingapp.shopping.cart.CartActivity;
import com.shoppingapp.shopping.orderListing.OrderListingActivity;
import com.shoppingapp.shopping.products.adapter.ProductsAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rajatarora.com.shoppingapp.R;

public class ProductActivity extends BaseActivity<ProductPresenter> implements ProductView, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.llError)
    LinearLayout llError;

    @BindView(R.id.llProducts)
    LinearLayout llProducts;

    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;

    @BindView(R.id.btnRetry)
    Button btnRetry;

    @BindView(R.id.pbLoader)
    ProgressBar pbLoader;

    @Inject
    ProductsAdapter mProductsAdapter;

    @Override
    protected int layout() {
        return R.layout.activity_products;
    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.products);
        }
        mToolBar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        setupRecyclerView();
        initListeners();
    }

    private void initListeners() {
        btnRetry.setOnClickListener(this);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen._1sdp);
        rvProducts.addItemDecoration(itemDecoration);
        rvProducts.setAdapter(mProductsAdapter);
        fetchProducts();
    }

    private void fetchProducts() {
        presenter.getProducts();
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
        presenter.getProducts();
    }

    @Override
    public void showProducts(List<Products.ProductsBean> products) {
        mProductsAdapter.setProducts(products);
        llProducts.setVisibility(View.VISIBLE);
    }

    @Override
    public void addToCart(Products.ProductsBean productsBean) {
        presenter.addProductToCart(productsBean);
    }

    @Override
    public void showAddToCartError(String localizedMessage) {
        showToast(localizedMessage);
    }

    @Override
    public void showAddToCartSucess() {
        showToast(getString(R.string.add_to_cart_success));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_menu:
                startActivity(OrderListingActivity.newIntent(ProductActivity.this));
                return true;
            case R.id.cart_menu:
                startActivity(CartActivity.newIntent(ProductActivity.this));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRetry:
                presenter.retry();
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(ProductActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
