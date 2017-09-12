package com.example.android.stolpersteinear;

/**
 * Via the menu entry "GPS location 03" and the "activate imageView" we select one
 * data entry and save two entries into the local database. After this check if
 * the entries are visible.
 * YOU HAVE TO RUN THIS WITHIN THE DEVEL environment!
 */

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.ContentValues.TAG;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SaveAndViewDataset {

    private final String[] permissions = {"android.permission.CAMERA",
            "android.permission.ACCESS_FINE_LOCATION"};

    @Rule
    public ActivityTestRule<MainScreenActivity> mActivityTestRule = new ActivityTestRule<>(MainScreenActivity.class);

    @Test
    public void saveAndViewDataset() {
        allowPermissions();
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("GPS location 03"), isDisplayed()));
        appCompatTextView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Activate Image"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.icon), withContentDescription("Stolpersteine Logo"),
                        withParent(allOf(withId(R.id.rlMainScreen),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        appCompatImageView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(357);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onData(anything())
                .inAdapterView(withId(R.id.detailsList))
                .atPosition(0)
                .onChildView(withId(R.id.saveDataSet))
                .check(matches(is(instanceOf(RadioButton.class))))
                .perform(click());

        try {
            Thread.sleep(357);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onData(anything())
                .inAdapterView(withId(R.id.detailsList))
                .atPosition(1)
                .onChildView(withId(R.id.saveDataSet))
                .check(matches(is(instanceOf(RadioButton.class))))
                .perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        withParent(withId(R.id.toolbar_details)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(358);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.title), withText("Saved items"), isDisplayed()));
        appCompatTextView3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(359);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onData(anything())
                .inAdapterView(withId(R.id.listViewStored))
                .atPosition(1)
                .onChildView(withId(R.id.textviewStoredVictimName))
                .check(matches(is(instanceOf(TextView.class))))
                .perform(click());

    }

    private void allowPermissions() {
        for (String permission : permissions) {
            InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                    "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName()
                            + " " + permission);
            Log.i(TAG, "Permission");
        }
    }

}
