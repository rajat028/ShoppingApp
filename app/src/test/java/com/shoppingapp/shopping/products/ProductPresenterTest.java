package com.shoppingapp.shopping.products;

import com.shoppingapp.data.LocalRepository;
import com.shoppingapp.data.ProductsRepository;
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
public class ProductPresenterTest {

    @Rule
    public RxSchedulersOverrideRule schedulersOverrideRule = new RxSchedulersOverrideRule();

    private static Products.ProductsBean INTERNAL_PRODUCT = new Products.ProductsBean("One Plus 6T",
            "www.google.com",
            "2000",
            2.00,
            false);
    private static List<Products.ProductsBean> INTERNAL_PRODUCTS = Collections.singletonList(INTERNAL_PRODUCT);
    private static Products PRODUCTS = new Products(INTERNAL_PRODUCTS);

    @Mock
    private ProductsRepository productRepository;
    @Mock
    private LocalRepository localRepository;
    @Mock
    private ProductView view;

    private Single<List<Products.ProductsBean>> internalProducts = Single.just(INTERNAL_PRODUCTS);
    private Single<Products> products = Single.just(PRODUCTS);
    private ProductPresenter productPresenter;

    @Before
    public void setUp() {
        productPresenter = new ProductPresenter(productRepository, localRepository);
        productPresenter.attachView(view);
        when(localRepository.getAllProducts()).thenReturn(internalProducts);
        when(productRepository.getProducts()).thenReturn(products);
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
        when(localRepository.getAllProducts())
                .thenReturn(Single.error(new IOException()));

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
                .thenReturn(Single.error(new IOException()));

        productPresenter.fetchProductsFromAPI();

        verify(view).hideLoader();
        verify(view).showError();
    }

    @Test
    public void shouldInsertProductInDatabase() {
        productPresenter.insertToLocal(PRODUCTS.getProducts());

        verify(localRepository).insertProduct(INTERNAL_PRODUCT);
    }

    @Test
    public void shouldAddProductToCart() {
        productPresenter.addProductToCart(INTERNAL_PRODUCT);

        verify(localRepository).updateProductStatus(INTERNAL_PRODUCT);
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