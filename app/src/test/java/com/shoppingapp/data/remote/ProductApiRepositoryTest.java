package com.shoppingapp.data.remote;

import com.shoppingapp.data.model.Products;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import io.reactivex.Observable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductApiRepositoryTest {

    @Mock
    private ShoppingApi shoppingApi;

    private ProductApiRepository productApiRepository;

    private Products.ProductsBean productsBean = new Products.ProductsBean("One Plus 6T",
            "www.google.com",
            "2000",
            2.00,
            false);
    private Products products = new Products(Collections.singletonList(productsBean));
    private Observable<Products> productsObservable = Observable.just(products);


    @Before
    public void setUp() {
        productApiRepository = new ProductApiRepository(shoppingApi);

        when(shoppingApi.getProducts()).thenReturn(productsObservable);
    }

    @Test
    public void shouldReturnGetProductsIsCalled() {
        Observable<Products> observable = productApiRepository.getProducts();

        verify(shoppingApi).getProducts();
        assertEquals(observable, productsObservable);
    }
}