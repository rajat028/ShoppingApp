package com.shoppingapp.data.remote;

import com.shoppingapp.data.model.Products;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductApiRepositoryTest {

    @Mock
    private ShoppingApi shoppingApi;

    private ProductApiRepository productApiRepository;

    private Products.ProductsBean PRODUCT = new Products.ProductsBean("One Plus 6T",
            "www.google.com",
            "2000",
            2.00,
            false);
    private Products PRODUCTS = new Products(Collections.singletonList(PRODUCT));
    private Single<Products> products = Single.just(PRODUCTS);


    @Before
    public void setUp() {
        productApiRepository = new ProductApiRepository(shoppingApi);

        when(shoppingApi.getProducts()).thenReturn(products);
    }

    @Test
    public void shouldReturnGetProductsIsCalled() {
        TestObserver<Products> testObserver = productApiRepository.getProducts().test();

        testObserver.assertValue(PRODUCTS);
    }
}