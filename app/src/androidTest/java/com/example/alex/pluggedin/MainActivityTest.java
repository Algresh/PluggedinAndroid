package com.example.alex.pluggedin;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

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


}
