package com.shoppingapp.shopping.orderDetail;

import com.shoppingapp.data.LocalRepository;
import com.shoppingapp.data.model.OrderModel;

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
public class OrderDetailPresenterTest {

    @Rule
    public RxSchedulersOverrideRule schedulersOverrideRule = new RxSchedulersOverrideRule();

    private static OrderModel ORDER_PRODUCTS = new OrderModel(1,
            "1234",
            "One Plus 6T",
            "www.google.com",
            "2000",
            2.00);
    private static List<OrderModel> ORDERED_PRODUCTS = Collections.singletonList(ORDER_PRODUCTS);
    private static String ORDER_ID = "1234";

    @Mock
    private LocalRepository localRepository;

    @Mock
    private OrderDetailView view;

    private Single<List<OrderModel>> orderedProducts = Single.just(ORDERED_PRODUCTS);
    private OrderDetailPresenter orderDetailPresenter;

    @Before
    public void setup() {
        orderDetailPresenter = new OrderDetailPresenter(localRepository);
        orderDetailPresenter.attachView(view);

        when(localRepository.getProductByOrderId(ORDER_ID)).thenReturn(orderedProducts);
    }

    @Test
    public void shouldReturnProductsAsPerOrderIdWhenSuccessful() {
        orderDetailPresenter.getOrderedProductById(ORDER_ID);

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showProducts(ORDERED_PRODUCTS);
    }

    @Test
    public void shouldReturnProductsAsPerOrderIdWhenFailed() {
        when(localRepository.getProductByOrderId(ORDER_ID))
                .thenReturn(Single.error(new IOException()));

        orderDetailPresenter.getOrderedProductById(ORDER_ID);

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showError();
    }
}