package com.shoppingapp.shopping.cart;


import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.shoppingapp.R;
import com.shoppingapp.data.Local.ShoppingDatabase;
import com.shoppingapp.data.model.Products;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CartActivityTest {

    @Rule
    public ActivityTestRule<CartActivity> cartActivityTestRule
            = new ActivityTestRule<>(CartActivity.class);

    private static Products.ProductsBean CART_PRODUCTS_BEAN = new Products.ProductsBean("OnePlus 6",
            "www.google.com",
            "Rs 40000",
            4.5,
            true);

    private ShoppingDatabase shoppingDatabase;

    @Before
    public void setUp() {
        shoppingDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                ShoppingDatabase.class).build();
        shoppingDatabase.productDAO().insertProduct(CART_PRODUCTS_BEAN);
    }

    @Test
    public void shouldMatchDisplayedDataWithFetchedCartProductsFromDatabse() {
        shoppingDatabase.productDAO().getCartProducts();
        cartActivityTestRule.launchActivity(null);
        onView(
                allOf(withId(R.id.tvProductName), withText("OnePlus 6"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvProducts), 0), 1),
                        isDisplayed()))
                .check(matches(withText("OnePlus 6")));

        onView(
                allOf(withId(R.id.tvProductPrice), withText("Rs 40000"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvProducts),
                                        0),
                                2),
                        isDisplayed()))
                .check(matches(withText("Rs 40000")));

        onView(
                allOf(withId(R.id.tvProductRating), withText("4.5"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvProducts), 0), 3),
                        isDisplayed()))
                .check(matches(withText("4.5")));

        onView(
                allOf(withId(R.id.ivAddToCart), withContentDescription("Add To Cart"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvProducts), 0), 1),
                        isDisplayed()))
                .check(matches(isDisplayed()));
    }

    @Test
    public void ensureNavigateToPrevious() {
        onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                0)),
                                0),
                        isDisplayed()))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    public void ensureOrderSubmitSuccessfully() {
        onView(
                allOf(withId(R.id.btnPlaceOrder),
                        childAtPosition(
                                allOf(withId(R.id.llCartDetails),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                3)),
                                1),
                        isDisplayed()))
                .check(matches(isDisplayed()))
                .perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @After
    void tearDown() {
        cartActivityTestRule.finishActivity();
    }
}
