package com.shoppingapp.data.Local;

import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class DatabaseRepositoryTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private
    ProductDAO productDAO;

    private DatabaseRepository databaseRepository;

    private Products.ProductsBean productsBean = new Products.ProductsBean();
    private OrderModel orderModel = new OrderModel("1234", productsBean);

    @Before
    public void setUp() {
        databaseRepository = new DatabaseRepository(productDAO);
    }

    @Test
    public void shouldReturnGetAllProductsIsCalled() {
        databaseRepository.getAllProducts();
    }

    @Test
    public void shouldReturnGetCartProductsIsCalled() {
        databaseRepository.getCartProducts();
    }

    @Test
    public void shouldReturnGetAllOrdersIsCalled() {
        databaseRepository.getAllOrder();
    }

    @Test
    public void shouldReturnGetOrderByProductIdIsCalled() {
        databaseRepository.getProductByOrderId("1");
    }

    @Test
    public void shouldReturnInsertProductIsCalled() {
        databaseRepository.insertProduct(productsBean);
    }

    @Test
    public void shouldReturnUpdateProductIsCalled() {
        databaseRepository.updateProductStatus(productsBean);
    }

    @Test
    public void shouldReturnUpdateAllProductsIsCalled() {
        databaseRepository.updateAllProducts(false, "One Plus 6T");
    }

    @Test
    public void shouldReturnInsertOrderIsCalled() {
        databaseRepository.insertOrder(orderModel);
    }

}