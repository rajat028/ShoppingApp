package com.shoppingapp.data.Local;

import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.data.model.Products;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Flowable;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseRepositoryTest {

    private static Products.ProductsBean PRODUCTS_BEAN = new Products.ProductsBean("One Plus 6T",
            "www.google.com",
            "2000",
            2.00,
            false);
    private static Products.ProductsBean CART_PRODUCTS_BEAN = new Products.ProductsBean("Samsung Galaxy",
            "www.google.com",
            "5000",
            8.00,
            true);
    private static String ORDER_ID = "1234";
    private static OrderModel ORDERED_PRODUCTS = OrderModel.create(ORDER_ID, CART_PRODUCTS_BEAN);
    private static List<Products.ProductsBean> PRODUCTS_BEAN_LIST =
            Collections.singletonList(PRODUCTS_BEAN);
    private static List<Products.ProductsBean> CART_PRODUCTS_BEAN_LIST =
            Arrays.asList(CART_PRODUCTS_BEAN, CART_PRODUCTS_BEAN);

    @Mock
    private ProductDAO productDAO;

    private DatabaseRepository databaseRepository;
    private Flowable<List<Products.ProductsBean>> productListFlowable
            = Flowable.just(PRODUCTS_BEAN_LIST);
    private Flowable<List<Products.ProductsBean>> cartListFlowable
            = Flowable.just(CART_PRODUCTS_BEAN_LIST);
    private Flowable<List<String>> orderListFlowable
            = Flowable.just(Collections.singletonList(ORDER_ID));
    private Flowable<List<OrderModel>> orderedProductListFlowable
            = Flowable.just(Collections.singletonList(ORDERED_PRODUCTS));

    @Before
    public void setUp() {
        databaseRepository = new DatabaseRepository(productDAO);
        when(productDAO.getAllProducts()).thenReturn(productListFlowable);
        when(productDAO.getCartProducts()).thenReturn(cartListFlowable);
        when(productDAO.getAllOrder()).thenReturn(orderListFlowable);
        when(productDAO.getProductByOrderId(ORDER_ID)).thenReturn(orderedProductListFlowable);
    }

    @Test
    public void shouldReturnAllProductsList() {
        Flowable<List<Products.ProductsBean>> allProducts = databaseRepository.getAllProducts();

        verify(productDAO).getAllProducts();
        assertEquals(allProducts, productListFlowable);
    }

    @Test
    public void shouldReturnAllCartProductsList() {
        Flowable<List<Products.ProductsBean>> allCartProducts = databaseRepository.getCartProducts();

        verify(productDAO).getCartProducts();
        assertEquals(allCartProducts, cartListFlowable);
    }

    @Test
    public void shouldReturnAllOrdersList() {
        Flowable<List<String>> allOrdersFlowable = databaseRepository.getAllOrder();

        verify(productDAO).getAllOrder();
        assertEquals(allOrdersFlowable, orderListFlowable);
    }

    @Test
    public void shouldReturnOrderedProductListFromOrderId() {
        Flowable<List<OrderModel>> productsListByOrderId = databaseRepository.getProductByOrderId(ORDER_ID);

        verify(productDAO).getProductByOrderId(ORDER_ID);
        assertEquals(productsListByOrderId, orderedProductListFlowable);
    }

    @Test
    public void shouldCallProductDaoInsertProduct() {
        databaseRepository.insertProduct(PRODUCTS_BEAN);

        verify(productDAO).insertProduct(PRODUCTS_BEAN);
    }

    @Test
    public void shouldCallProductDaoUpdateProductStatus() {
        databaseRepository.updateProductStatus(PRODUCTS_BEAN);

        verify(productDAO).updateProductStatus(PRODUCTS_BEAN);
    }

    @Test
    public void shouldCallProductDaoUpdateAllProducts() {
        databaseRepository.updateAllProducts(false, "One Plus 6T");

        verify(productDAO).updateAllProducts(false, "One Plus 6T");
    }

    @Test
    public void shouldCallProductDaoInsertOrder() {
        databaseRepository.insertOrder(ORDERED_PRODUCTS);

        verify(productDAO).insertOrder(ORDERED_PRODUCTS);
    }

}