package com.example.espressobug;

import android.view.View;

import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TextInputErrorTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void performTest() {
        // check the main activity text view, for fun
        onView(withId(R.id.textView)).check(matches(withText("Hello World!")));

        // press the dialog button
        onView(withId(R.id.showDialogButton)).perform(click());

        // make sure the dialog is up by checking the title
        onView(withId(R.id.text)).check(matches(withText("This is a test")));

        // enter some text and hit OK
        onView(withId(R.id.data_entry)).perform(typeText("bbb"), closeSoftKeyboard());

        //////////////////////////////////
        // The click() will hang forever!
        //////////////////////////////////
        onView(withId(R.id.button_0)).perform(click());

        // we never get to this line
        // if you comment out the .setError on line 89 of MainActivity then you will reach this line
        // The test will fail in that case of course, but at least it completes
        // For some reason, calling .setError causes espresso to hang on the button click before this
        onView(withId(R.id.data_entry_layout)).check(matches(hasTextInputLayoutErrorText("You entered the wrong text!")));

    }

    /**
     * Simple matcher to check the error text on a TextInputLayout
     */
    public static Matcher<View> hasTextInputLayoutErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }

                CharSequence error = ((TextInputLayout) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
