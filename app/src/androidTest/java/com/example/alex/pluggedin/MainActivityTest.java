package com.example.alex.pluggedin;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.core.AllOf.allOf;
//import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static com.android.support.test.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    final int NUMBER_TABS = 5;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testSearchFindSomething() throws Exception {
        onView(withId(R.id.action_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("Injustice"), pressImeActionButton());
        onView(withId(R.id.recycleViewSearch)).check(matches(isDisplayed()));
    }

    public void testSearchFindNothing() throws Exception {
        onView(withId(R.id.action_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("No such article"), pressImeActionButton());
        String str = getActivity().getResources().getString(R.string.find_nothing);
        onView(withId(R.id.search_empty)).check(matches(withText(str)));
    }

    public void testSearchWithEmptyString() throws Exception {
        onView(withId(R.id.action_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText(""), pressImeActionButton());
        String str = getActivity().getResources().getString(R.string.find_nothing);
        try {onView(withId(R.id.search_empty)).check(matches(withText(str)));} catch (NoMatchingViewException e) {}
    }

    public void testSearchWithOneLetter() throws Exception {
        onView(withId(R.id.action_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("A"), pressImeActionButton());
        String str = getActivity().getResources().getString(R.string.find_nothing);
        try {onView(withId(R.id.search_empty)).check(matches(withText(str)));} catch (NoMatchingViewException e) {}
    }

    public void testAllTabDisplayedOnSwipe() throws Exception {
        for(int i = 0; i < NUMBER_TABS - 1; i++) {
            onView(withId(R.id.viewPager)).perform(swipeLeft());
        }
    }

//    public void testTestSelectArticle() throws Exception {
////        onView(withId(R.id.viewPager)).perform(swipeLeft());
//
////        onView(allOf(withId(R.id.recycleView), isDisplayed())).perform(click());
////        onData(allOf(withId(R.id.recycleView), isDescendantOfA(withId(R.id.viewPager))));
////        onData(allOf(is(instanceOf(CardView.class)))).atPosition(1).;
////        onData(allOf(withId(R.id.recycleView)))
//        onView(allOf(withId(R.id.cardView), isDescendantOfA(withId(R.id.viewPager))))
//                .perform(click());
//    }

//    public static Matcher<View> firstChildOf(final Matcher<View> parentMatcher) {
//        return new TypeSafeMatcher<View>() {
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("with first child view of type parentMatcher");
//            }
//
//            @Override
//            public boolean matchesSafely(View view) {
//
//                if (!(view.getParent() instanceof ViewGroup)) {
//                    return parentMatcher.matches(view.getParent());
//                }
//                ViewGroup group = (ViewGroup) view.getParent();
//                return parentMatcher.matches(view.getParent()) && group.getChildAt(0).equals(view);
//
//            }
//        };
//    }

    //    public static Matcher<Object> withItemContent(String expectedText) {
//        checkNotNull(expectedText);
//        return withItemContent(equalTo(expectedText));
//    }
//    public static Matcher<Object> withItemContent(final Matcher<String> itemTextMatcher) {
//        checkNotNull(itemTextMatcher);
//        return new BoundedMatcher<Object, String>(String.class) {
//            @Override
//            public boolean matchesSafely(String text) {
//                return itemTextMatcher.matches(text);
//            }
//
//            @Override
//            public void describeTo(Description description) {
//                description.appendText("with item content: ");
//                itemTextMatcher.describeTo(description);
//            }
//        };
//    }
}
