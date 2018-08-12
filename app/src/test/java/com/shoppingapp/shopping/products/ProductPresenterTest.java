package com.shoppingapp.shopping.products;

import com.shoppingapp.data.ProductRepository;
import com.shoppingapp.data.ShoppingRepository;
import com.shoppingapp.data.model.Products;
import com.shoppingapp.data.remote.ShoppingApi;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductPresenterTest {


    private ProductPresenter productPresenter;

    private Products.ProductsBean products = new Products.ProductsBean();

    private Flowable<List<Products.ProductsBean>> productListFlowable= Flowable.just(Collections.singletonList(products));

    @Mock
    ProductRepository productRepository;
    @Mock
    ShoppingRepository shoppingRepository;
    @Mock
    ProductView view;

    @Before
    public void setUp() {
        productPresenter = new ProductPresenter(productRepository, shoppingRepository);
        when(shoppingRepository.getAllProducts()).thenReturn(productListFlowable);
    }

    @Test
    public void demo() {
        productPresenter.getProducts();

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view).showLoader();
        inOrder.verify(view).hideError();
    }
}