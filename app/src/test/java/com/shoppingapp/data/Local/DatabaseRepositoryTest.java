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

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseRepositoryTest {

    private static String ORDER_ID = "1234";
    private static Products.ProductsBean PRODUCTS_BEAN = new Products.ProductsBean("One Plus 6T",
            "www.google.com",
            "2000",
            2.00,
            false);
    private static Products.ProductsBean CART_PRODUCT = new Products.ProductsBean("Samsung Galaxy",
            "www.google.com",
            "5000",
            8.00,
            true);
    private static List<String> ORDERS = Collections.singletonList(ORDER_ID);
    private static OrderModel ORDERED_PRODUCT = OrderModel.create(ORDER_ID, CART_PRODUCT);
    private static List<Products.ProductsBean> PRODUCTS = Collections.singletonList(PRODUCTS_BEAN);
    private static List<Products.ProductsBean> CART_PRODUCTS = Arrays.asList(CART_PRODUCT, CART_PRODUCT);
    private static List<OrderModel> ORDERED_PRODUCTS = Arrays.asList(ORDERED_PRODUCT, ORDERED_PRODUCT);

    @Mock
    private ProductDAO productDAO;

    private DatabaseRepository databaseRepository;
    private Single<List<Products.ProductsBean>> products = Single.just(PRODUCTS);
    private Single<List<Products.ProductsBean>> cartProducts
            = Single.just(CART_PRODUCTS);
    private Single<List<String>> orders
            = Single.just(ORDERS);
    private Single<List<OrderModel>> orderedProducts = Single.just(ORDERED_PRODUCTS);

    @Before
    public void setUp() {
        databaseRepository = new DatabaseRepository(productDAO);
        when(productDAO.getAllProducts()).thenReturn(products);
        when(productDAO.getCartProducts()).thenReturn(cartProducts);
        when(productDAO.getAllOrder()).thenReturn(orders);
        when(productDAO.getProductByOrderId(ORDER_ID)).thenReturn(orderedProducts);
    }

    @Test
    public void shouldReturnAllProductsList() {
        TestObserver<List<Products.ProductsBean>> testObserver = databaseRepository.getAllProducts().test();

        testObserver.assertValue(PRODUCTS);
    }

    @Test
    public void shouldReturnAllCartProductsList() {
        TestObserver<List<Products.ProductsBean>> testObserver = databaseRepository.getCartProducts().test();

        testObserver.assertValue(CART_PRODUCTS);
    }

    @Test
    public void shouldReturnAllOrdersList() {
        TestObserver<List<String>> testObserver = databaseRepository.getAllOrder().test();

        testObserver.assertValue(ORDERS);
    }

    @Test
    public void shouldReturnOrderedProductListFromOrderId() {
        TestObserver<List<OrderModel>> testObserver = databaseRepository.getProductByOrderId(ORDER_ID).test();

        testObserver.assertValue(ORDERED_PRODUCTS);
    }

    @Test
    public void shouldCallProductDaoInsertProduct() {
        TestObserver<Void> testObserver = databaseRepository.insertProduct(PRODUCTS_BEAN).test();

        testObserver.assertComplete();
    }

    @Test
    public void shouldCallProductDaoUpdateProductStatus() {
        TestObserver<Void> testObserver = databaseRepository.updateProductStatus(PRODUCTS_BEAN).test();

        testObserver.assertComplete();
    }

    @Test
    public void shouldCallProductDaoUpdateAllProducts() {
        TestObserver<Void> testObserver = databaseRepository.updateAllProducts(false, "One Plus 6T").test();

        testObserver.assertComplete();
    }

    @Test
    public void shouldCallProductDaoInsertOrder() {
        TestObserver<Void> testObserver = databaseRepository.insertOrder(ORDERED_PRODUCT).test();

        testObserver.assertComplete();
    }

}