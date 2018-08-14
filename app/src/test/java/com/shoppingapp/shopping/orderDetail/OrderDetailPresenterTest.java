package com.shoppingapp.shopping.orderDetail;

import com.shoppingapp.data.ShoppingRepository;
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

    private static OrderModel ORDER_MODEL = new OrderModel(1,
            "1234",
            "One Plus 6T",
            "www.google.com",
            "2000",
            2.00);
    private static List<OrderModel> ORDER_MODEL_PRODUCT_LIST = Collections.singletonList(ORDER_MODEL);

    @Mock
    private ShoppingRepository shoppingRepository;

    @Mock
    private OrderDetailView view;

    private Flowable<List<OrderModel>> orderListFlowable
            = Flowable.just(ORDER_MODEL_PRODUCT_LIST);
    private OrderDetailPresenter orderDetailPresenter;

    @Before
    public void setup() {
        orderDetailPresenter = new OrderDetailPresenter(shoppingRepository);
        orderDetailPresenter.attachView(view);

        when(shoppingRepository.getProductByOrderId("1234")).thenReturn(orderListFlowable);
    }

    @Test
    public void shouldReturnProductsAsPerOrderIdIsSuccessful() {
        orderDetailPresenter.getOrderedProductById("1234");

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showProducts(ORDER_MODEL_PRODUCT_LIST);
    }

    @Test
    public void shouldReturnProductsAsPerOrderIdIsFailed() {
        when(shoppingRepository.getProductByOrderId("1234"))
                .thenReturn(Flowable.<List<OrderModel>>error(new IOException()));

        orderDetailPresenter.getOrderedProductById("1234");

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showError();
    }

}