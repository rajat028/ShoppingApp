package com.shoppingapp.shopping.cart;

import com.shoppingapp.data.ShoppingRepository;
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

import io.reactivex.Flowable;
import utils.RxSchedulersOverrideRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartPresenterTest {

    @Rule
    public RxSchedulersOverrideRule schedulersOverrideRule = new RxSchedulersOverrideRule();

    private static Products.ProductsBean CART_PRODUCTS_BEAN = new Products.ProductsBean("One Plus 6T",
            "www.google.com",
            "2000",
            2.00,
            true);
    private static List<Products.ProductsBean> CART_PRODUCTS_BEAN_LIST = Collections.singletonList(CART_PRODUCTS_BEAN);

    @Mock
    private ShoppingRepository shoppingRepository;
    @Mock
    private CartView view;

    private Flowable<List<Products.ProductsBean>> cartProductsListFlowable
            = Flowable.just(Collections.singletonList(CART_PRODUCTS_BEAN));
    private CartPresenter cartPresenter;

    @Before
    public void setUp() {
        cartPresenter = new CartPresenter(shoppingRepository);
        cartPresenter.attachView(view);
        when(shoppingRepository.getCartProducts()).thenReturn(cartProductsListFlowable);
    }

    @Test
    public void shouldReturnCartProductsWhenDatabaseFetchingIsSuccessful() {
        cartPresenter.getCartProducts();

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showProducts(CART_PRODUCTS_BEAN_LIST);
    }

    @Test
    public void shouldReturnErrorWhenDatabaseFetchingIsFailed() {
        when(shoppingRepository.getCartProducts())
                .thenReturn(Flowable.<List<Products.ProductsBean>>error(new IOException()));

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
        cartPresenter.removeProductFromCart(CART_PRODUCTS_BEAN);

        verify(shoppingRepository).updateProductStatus(CART_PRODUCTS_BEAN);
        verify(view).showRemoveSucessMessage();
    }

    @Test
    public void shouldPlaceOrderSuccessfully() {
        cartPresenter.placeOrder(CART_PRODUCTS_BEAN_LIST);

        verify(view).completeOrder();
    }

    @After
    public void tearDown() {
        cartPresenter.detachView();
    }

}