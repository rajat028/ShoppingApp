package com.shoppingapp.shopping.cart;

import com.shoppingapp.data.LocalRepository;
import com.shoppingapp.data.model.Products;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import utils.RxSchedulersOverrideRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPresenterTest {

    @Rule
    public RxSchedulersOverrideRule schedulersOverrideRule = new RxSchedulersOverrideRule();

    private static Products.ProductsBean CART_PRODUCT = new Products.ProductsBean("One Plus 6T",
            "www.google.com",
            "2000",
            2.00,
            true);
    private static List<Products.ProductsBean> CART_PRODUCTS = Collections.singletonList(CART_PRODUCT);

    @Mock
    private LocalRepository localRepository;
    @Mock
    private CartView view;

    private Single<List<Products.ProductsBean>> cartProducts
            = Single.just(Collections.singletonList(CART_PRODUCT));
    private CartPresenter cartPresenter;

    @Before
    public void setUp() {
        cartPresenter = new CartPresenter();
        cartPresenter.attachView(view);
        when(localRepository.getCartProducts()).thenReturn(cartProducts);

    }

    @Test
    public void shouldReturnCartProductsWhenDatabaseFetchingIsSuccessful() {
        cartPresenter.getCartProducts();

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showProducts(CART_PRODUCTS);
    }

    @Test
    public void shouldReturnErrorWhenDatabaseFetchingIsFailed() {
        when(localRepository.getCartProducts())
                .thenReturn(Single.error(new IOException()));

        cartPresenter.getCartProducts();

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showError();
    }

    @Test
    public void shouldRetryWhenRetryIsCalled() {
        cartPresenter.retry();

        verify(view).retry();
    }

    @Test
    public void shouldRemoveProductFromCartSuccessfully() {
        cartPresenter.removeProductFromCart(CART_PRODUCT);

        verify(localRepository).updateProductStatus(CART_PRODUCT);
        verify(view).showRemoveSucessMessage();
    }

    @Test
    public void shouldPlaceOrderSuccessfully() {
        cartPresenter.placeOrder(CART_PRODUCTS);

        verify(view).completeOrder();
    }

    @After
    public void tearDown() {
        cartPresenter.detachView();
    }

}