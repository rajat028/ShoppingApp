package com.shoppingapp.shopping.products;

import com.shoppingapp.data.ProductRepository;
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
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private
    ProductRepository productRepository;
    @Mock
    private
    ShoppingRepository shoppingRepository;
    @Mock
    private
    ProductView view;

    private Products.ProductsBean productsBean = new Products.ProductsBean();
    private Products products = new Products();
    private Flowable<List<Products.ProductsBean>> productListFlowable
            = Flowable.just(Collections.singletonList(productsBean));
    private Observable<Products> productsObservable = Observable.just(products);

    private ProductPresenter productPresenter;


    @Before
    public void setUp() {
        productPresenter = new ProductPresenter(productRepository, shoppingRepository);
        productPresenter.attachView(view);
        addDummyValues();
        when(shoppingRepository.getAllProducts()).thenReturn(productListFlowable);
        when(productRepository.getProducts()).thenReturn(productsObservable);
    }

    private void addDummyValues() {
        List<Products.ProductsBean> productsBeanList = new ArrayList<>();
        productsBean.setName("One Plus 6T");
        productsBean.setImage_url("www.google.com");
        productsBean.setPrice("2000");
        productsBean.setRating(2.00);
        productsBean.setAddedToCart(false);
        productsBeanList.add(productsBean);
        products.setProducts(productsBeanList);
    }

    @Test
    public void shouldReturnProductsWhenDatabaseFetchingIsSuccessful() {
        productPresenter.getProducts();
        verify(view).showLoader();
        verify(view).hideError();
        verify(view).hideLoader();
        verify(view).showProducts(products.getProducts());
    }

    @Test
    public void shouldShowErrorWhenDatabaseFetchingIsFailed() {
        when(shoppingRepository.getAllProducts())
                .thenReturn(Flowable.<List<Products.ProductsBean>>error(new IOException()));
        productPresenter.getProducts();
        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showError();
    }

    @Test
    public void shouldReturnProductsWhenApiFetchingIsSuccessful() {
        productPresenter.fetchProductsFromAPI();
        verify(view).hideLoader();
        verify(view).showProducts(products.getProducts());
    }

    @Test
    public void shouldShowErrorWhenApiFetchingIsFailed() {
        when(productRepository.getProducts())
                .thenReturn(Observable.<Products>error(new IOException()));
        productPresenter.fetchProductsFromAPI();
        verify(view).hideLoader();
        verify(view).showError();
    }

    //Todo addProductTOCart Success and Failure

    @Test
    public void shouldInsertInDatabase() {
        productPresenter.insertToLocal(products.getProducts());
    }

    @Test
    public void shouldAddProductToCart() {
        productPresenter.addProductToCart(productsBean);
    }

    @Test
    public void shouldRetryWhenRetryIsCalled() {
        productPresenter.retry();
        verify(view).retry();
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
        productPresenter.detachView();
    }

}