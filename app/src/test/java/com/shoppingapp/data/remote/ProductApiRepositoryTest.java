package com.shoppingapp.data.remote;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class ProductApiRepositoryTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private
    ShoppingApi shoppingApi;

    private ProductApiRepository productApiRepository;

    @Before
    public void setUp() {
        productApiRepository = new ProductApiRepository(shoppingApi);
    }

    @Test
    public void shouldReturnGetProductsIsCalled() {
        productApiRepository.getProducts();
    }
}