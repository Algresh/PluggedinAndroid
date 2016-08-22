package ru.tulupov.alex.pluggedin;

import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;

import ru.tulupov.alex.pluggedin.R;

import ru.tulupov.alex.pluggedin.activities.SettingActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class SettingActivityTest extends ActivityInstrumentationTestCase2<SettingActivity> {
    public SettingActivityTest() {
        super(SettingActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testSwitchNotify() throws Exception {
        onView(ViewMatchers.withId(R.id.switchNotifyPermission)).check(matches(isChecked()));
        onView(withId(R.id.switchNotifyPermission)).perform(click());
        onView(withId(R.id.switchNotifyPermission)).check(matches(isNotChecked()));
    }


    public void testSwitchSound() throws Exception {
        onView(withId(R.id.switchSoundPermission)).check(matches(isNotChecked()));
        onView(withId(R.id.switchSoundPermission)).perform(click());
        onView(withId(R.id.switchSoundPermission)).check(matches(isChecked()));
    }

    public void testSwitchChromeTabs() throws Exception {
        onView(withId(R.id.switchChromeTabs)).check(matches(isNotChecked()));
        onView(withId(R.id.switchChromeTabs)).perform(click());
        onView(withId(R.id.switchChromeTabs)).check(matches(isChecked()));
    }

    public void testChangeFontSize() throws Exception {
        String smallFontSize = getActivity().getResources()
                .getStringArray(R.array.typesFontSize)[0];

        onView(withId(R.id.fontSize)).perform(click());
        onView(withText(smallFontSize)).perform(click());
        onView(withId(R.id.fontSize)).perform(click());
        onView(withText(smallFontSize)).check(matches(isChecked()));
    }


}
