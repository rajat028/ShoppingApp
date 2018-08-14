package com.shoppingapp.shopping.cart;

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

public class CartPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private
    ShoppingRepository shoppingRepository;

    @Mock
    private
    CartView view;

    private Products.ProductsBean productsBean = new Products.ProductsBean();
    private Flowable<List<Products.ProductsBean>> productListFlowable
            = Flowable.just(Collections.singletonList(productsBean));
    private List<Products.ProductsBean> productsBeanList = new ArrayList<>();

    private CartPresenter cartPresenter;

    @Before
    public void setUp() {
        cartPresenter = new CartPresenter(shoppingRepository);
        cartPresenter.attachView(view);
        addDummyValues();
        when(shoppingRepository.getCartProducts()).thenReturn(productListFlowable);
    }

    private void addDummyValues() {
        productsBean.setName("One Plus 6T");
        productsBean.setImage_url("www.google.com");
        productsBean.setPrice("2000");
        productsBean.setRating(2.00);
        productsBean.setAddedToCart(true);
        productsBeanList.add(productsBean);
    }

    @Test
    public void shouldReturnCartProductsWhenDatabaseFetchingIsSuccessful() {
        cartPresenter.getCartProducts();
        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showProducts(productsBeanList);
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
    public void retry() {
        cartPresenter.retry();
        verify(view).retry();
    }

    @Test
    public void shouldRemoveProductFromCartIsSuccessful() {
        cartPresenter.removeProductFromCart(productsBean);
        verify(view).showRemoveSucessMessage();
    }

    @Test
    public void shouldRemoveProductFromCartIsFailed() {
        cartPresenter.removeProductFromCart(productsBean);
        verify(view).showRemoveErrorMessage("Failed");
        // TODO Pass Exception as a parameter
    }

    @Test
    public void shouldPlaceOrderIsSuccessful() {
        cartPresenter.placeOrder((ArrayList<Products.ProductsBean>) productsBeanList);
        view.completeOrder();
    }

    @Test
    public void shouldPlaceOrderIsFailed() {
        cartPresenter.placeOrder((ArrayList<Products.ProductsBean>) productsBeanList);
        view.hideLoader();
        view.showError();
        // TODO Pass Exception as a parameter
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
        cartPresenter.detachView();
    }

}