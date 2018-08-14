package com.shoppingapp.shopping.products;

import com.shoppingapp.data.ProductRepository;
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
import io.reactivex.Observable;
import utils.RxSchedulersOverrideRule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductPresenterTest {

    @Rule
    public RxSchedulersOverrideRule schedulersOverrideRule = new RxSchedulersOverrideRule();

    private static Products.ProductsBean PRODUCTS_BEAN = new Products.ProductsBean("One Plus 6T",
            "www.google.com",
            "2000",
            2.00,
            false);
    private static List<Products.ProductsBean> PRODUCTS_BEAN_LIST = Collections.singletonList(PRODUCTS_BEAN);
    private static Products PRODUCTS = new Products(PRODUCTS_BEAN_LIST);

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ShoppingRepository shoppingRepository;
    @Mock
    private ProductView view;

    private Flowable<List<Products.ProductsBean>> productListFlowable
            = Flowable.just(PRODUCTS_BEAN_LIST);
    private Observable<Products> productsObservable = Observable.just(PRODUCTS);
    private ProductPresenter productPresenter;

    @Before
    public void setUp() {
        productPresenter = new ProductPresenter(productRepository, shoppingRepository);
        productPresenter.attachView(view);
        when(shoppingRepository.getAllProducts()).thenReturn(productListFlowable);
        when(productRepository.getProducts()).thenReturn(productsObservable);
    }

    @Test
    public void shouldReturnProductsWhenDatabaseFetchingIsSuccessful() {
        productPresenter.getProducts();

        verify(view).hideError();
        verify(view).showLoader();
        verify(view).hideLoader();
        verify(view).showProducts(PRODUCTS.getProducts());
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
        verify(view).showProducts(PRODUCTS.getProducts());
    }

    @Test
    public void shouldShowErrorWhenApiFetchingIsFailed() {
        when(productRepository.getProducts())
                .thenReturn(Observable.<Products>error(new IOException()));

        productPresenter.fetchProductsFromAPI();

        verify(view).hideLoader();
        verify(view).showError();
    }

    @Test
    public void shouldInsertProductInDatabase() {
        productPresenter.insertToLocal(PRODUCTS.getProducts());

        verify(shoppingRepository).insertProduct(PRODUCTS_BEAN);
    }

    @Test
    public void shouldAddProductToCart() {
        productPresenter.addProductToCart(PRODUCTS_BEAN);

        verify(shoppingRepository).updateProductStatus(PRODUCTS_BEAN);
        verify(view).showAddToCartSucess();
    }

    @Test
    public void shouldRetryWhenRetryIsCalled() {
        productPresenter.retry();

        verify(view).retry();
    }

    @After
    public void tearDown() {
        productPresenter.detachView();
    }

}