package com.shoppingapp.shopping.products;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.shoppingapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.appflate.restmock.RESTMockServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.appflate.restmock.utils.RequestMatchers.pathContains;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProductActivityTest {

    @Rule
    public ActivityTestRule<ProductActivity> productActivityTestRule
            = new ActivityTestRule<>(ProductActivity.class);

    @Before
    public void setUp() {
        RESTMockServer.reset();
    }

    @Test
    public void shouldMatchDisplayedDataWithGivenMockDataOnSuccess() {
        RESTMockServer.whenGET(pathContains("products.json"))
                .thenReturnFile(200, "products/products.json");
        productActivityTestRule.launchActivity(null);

        onView(allOf(withId(R.id.ivProduct), withContentDescription("ShoppingApi"),
                childAtPosition(
                        childAtPosition(
                                withId(R.id.rvProducts), 0), 0)))
                .check(matches(isDisplayed()));

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
                                        withId(R.id.rvProducts), 0), 2),
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
                allOf(withId(R.id.tvProductName), withText("Canon Eos 1300D 18MP Digital SLR"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvProducts), 3), 1),
                        isDisplayed()))
                .check(matches(withText("Canon Eos 1300D 18MP Digital SLR")));

        onView(
                allOf(withId(R.id.tvProductPrice), withText("Rs 30990"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvProducts), 3), 2),
                        isDisplayed()))
                .check(matches(withText("Rs 30990")));

        onView(
                allOf(withId(R.id.tvProductRating), withText("4.2"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvProducts), 3), 3),
                        isDisplayed()))
                .check(matches(withText("4.2")));

        onView(
                allOf(withId(R.id.ivAddToCart), withContentDescription("Add To Cart"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rvProducts),
                                        0),
                                1),
                        isDisplayed()))
                .check(matches(isDisplayed()));

        onView(
                allOf(withId(R.id.order_menu), withContentDescription("Orders"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()))
                .check(matches(withText("Orders")));
    }

    @Test
    public void ensureNavigateToCart() {
        onView(
                allOf(withId(R.id.cart_menu), withContentDescription("Cart"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                1),
                        isDisplayed()))
                .check(matches(withText("Cart")))
                .perform(click());
    }

    @Test
    public void ensureNavigateToOrders() {
        onView(
                allOf(withId(R.id.order_menu), withContentDescription("Orders"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        1),
                                0),
                        isDisplayed()))
                .check(matches(withText("Orders")))
                .perform(click());
    }

    @Test
    public void checkIfErrorScreenIsDisplayedOnError() {
        RESTMockServer.whenGET(pathContains("products.json"))
                .thenReturnFile(501, "products/products.json");
        productActivityTestRule.launchActivity(new Intent());

        String errorMessage = getString(R.string.error_message);
        String retryButtonText = getString(R.string.retry_button_text);

        onView(withId(R.id.tvError))
                .check(matches(withText(errorMessage)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.btnRetry))
                .check(matches(withText(retryButtonText)))
                .check(matches(isDisplayed()));

    }

    private String getString(int id) {
        return productActivityTestRule.getActivity().getString(id);
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
        productActivityTestRule.finishActivity();
    }
}
