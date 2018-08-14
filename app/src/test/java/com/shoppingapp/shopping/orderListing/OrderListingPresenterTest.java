package com.shoppingapp.shopping.orderListing;

import com.shoppingapp.data.ShoppingRepository;
import com.shoppingapp.data.model.Products;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderListingPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private
    ShoppingRepository shoppingRepository;

    @Mock
    private
    OrderListingView view;

    private String dummyOrderId = "1234";

    private Flowable<List<String>> orderListFlowable
            = Flowable.just(Collections.singletonList(dummyOrderId));

    private ArrayList<String> dummyArrayList = new ArrayList<>();

    private OrderListingPresenter orderListingPresenter;

    @Before
    public void setup() {
        orderListingPresenter = new OrderListingPresenter(shoppingRepository);
        orderListingPresenter.attachView(view);

        dummyArrayList.add(dummyOrderId);
        when(shoppingRepository.getAllOrder()).thenReturn(orderListFlowable);
    }

    @Test
    public void shouldReturnOrdersWhenDatabaseFetchingIsSuccessful() {
        orderListingPresenter.getAllOrders();
        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showOrders(dummyArrayList);
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

    @BeforeClass
    public static void setUpRxSchedulers() {
        final Scheduler immediate = new Scheduler() {
            @Override
            public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit);
            }

            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(new Executor() {
                    @Override
                    public void execute(@android.support.annotation.NonNull Runnable runnable) {
                        runnable.run();
                    }
                });
            }
        };

        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) {
                return immediate;
            }
        });
        RxJavaPlugins.setInitComputationSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) {
                return immediate;
            }
        });
        RxJavaPlugins.setInitNewThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) {
                return immediate;
            }
        });
        RxJavaPlugins.setInitSingleSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) {
                return immediate;
            }
        });
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) {
                return immediate;
            }
        });
    }

    @After
    public void tearDown() {
        orderListingPresenter.detachView();
    }
}