package com.shoppingapp.shopping.orderListing;

import com.shoppingapp.data.ShoppingRepository;

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
public class OrderListingPresenterTest {

    @Rule
    public RxSchedulersOverrideRule schedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock
    private ShoppingRepository shoppingRepository;

    @Mock
    private OrderListingView view;

    private List<String> orderList = Collections.singletonList("1234");
    private Flowable<List<String>> orderListFlowable
            = Flowable.just(orderList);

    private OrderListingPresenter orderListingPresenter;

    @Before
    public void setup() {
        orderListingPresenter = new OrderListingPresenter(shoppingRepository);
        orderListingPresenter.attachView(view);
        when(shoppingRepository.getAllOrder()).thenReturn(orderListFlowable);
    }

    @Test
    public void shouldReturnOrdersWhenDatabaseFetchingIsSuccessful() {
        orderListingPresenter.getAllOrders();

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showOrders(orderList);
    }

    @Test
    public void shouldShowErrorWhenDatabaseFetchingIsFailed() {
        when(shoppingRepository.getAllOrder())
                .thenReturn(Flowable.<List<String>>error(new IOException()));

        orderListingPresenter.getAllOrders();

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showError();
    }

    @After
    public void tearDown() {
        orderListingPresenter.detachView();
    }
}