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

import io.reactivex.Flowable;
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
    private static List<OrderModel> ORDERED_PRODUCT_LIST = Collections.singletonList(ORDER_PRODUCTS);
    private static String ORDER_ID = "1234";

    @Mock
    private LocalRepository localRepository;

    @Mock
    private OrderDetailView view;

    private Flowable<List<OrderModel>> orderListFlowable = Flowable.just(ORDERED_PRODUCT_LIST);
    private OrderDetailPresenter orderDetailPresenter;

    @Before
    public void setup() {
        orderDetailPresenter = new OrderDetailPresenter(localRepository);
        orderDetailPresenter.attachView(view);

        when(localRepository.getProductByOrderId(ORDER_ID)).thenReturn(orderListFlowable);
    }

    @Test
    public void shouldReturnProductsAsPerOrderIdWhenSuccessful() {
        orderDetailPresenter.getOrderedProductById(ORDER_ID);

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showProducts(ORDERED_PRODUCT_LIST);
    }

    @Test
    public void shouldReturnProductsAsPerOrderIdWhenFailed() {
        when(localRepository.getProductByOrderId(ORDER_ID))
                .thenReturn(Flowable.<List<OrderModel>>error(new IOException()));

        orderDetailPresenter.getOrderedProductById(ORDER_ID);

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showError();
    }
}