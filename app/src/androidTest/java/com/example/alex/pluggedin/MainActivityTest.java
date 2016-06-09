package com.example.alex.pluggedin;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
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
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static com.android.support.test.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testSelectArticle() throws Exception {
//        ViewInteraction fragmentText = onView(withId(R.id.));

//        onView(allOf(withId(R.id.button), isDescendantOfA(firstChildOf(withId(R.id.viewpager)))))
//                .perform(click());

//        ViewInteraction interaction = onView(allOf(withId(R.id.cardView), isDescendantOfA(firstChildOf(withId(R.id.viewPager)))));
//
//        ViewInteraction viewPager = onView(withId(R.id.viewPager));
//        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(allOf(withId(R.id.cardView), isDisplayed())).perform(click());


//        onView(withId(R.id.recycleView))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
    }

    public void testSearchFindNothing() throws Exception {
        onView(withId(R.id.action_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("No such article"), pressImeActionButton());
        String str = getActivity().getResources().getString(R.string.find_nothing);
        onView(withId(R.id.search_empty)).check(matches(withText(str)));
    }

    public void testSearchFindSomething() throws Exception {
        onView(withId(R.id.action_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("Injustice"), pressImeActionButton());
        onView(withId(R.id.recycleViewSearch)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
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
