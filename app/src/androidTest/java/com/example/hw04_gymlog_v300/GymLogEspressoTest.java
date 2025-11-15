package com.example.hw04_gymlog_v300;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.platform.app.InstrumentationRegistry;
import com.example.hw04_gymlog_v300.database.AppDataBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is an automated UI test (Espresso) that runs on the Android Emulator
 * It simulates a real user tapping buttons and typing text to ensure the main flow works
 */
@RunWith(AndroidJUnit4.class)
public class GymLogEspressoTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // This runs before every test to wipe the database clean.
    // to ensures we start with a fresh state so no old logs or weird login states
    @Before
    public void clearDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        AppDataBase.getDatabase(context).clearAllTables();
    }

    @Test
    public void loginAddLogAndVerify() {
        // 1. Login Process
        // We find the username box, type "admin1", and close the keyboard so it doesn't block other views
        onView(withId(R.id.usernameLoginEditText)).perform(typeText("admin1"), closeSoftKeyboard());
        onView(withId(R.id.passwordLoginEditText)).perform(typeText("admin1"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // 2. Add a new Log Entry
        String exercise = "Bench Press";
        onView(withId(R.id.exerciseInputEditText)).perform(typeText(exercise), closeSoftKeyboard());
        onView(withId(R.id.weightInputEditText)).perform(typeText("150"), closeSoftKeyboard());
        onView(withId(R.id.repInputEditText)).perform(typeText("5"), closeSoftKeyboard());
        onView(withId(R.id.logButton)).perform(click());

        // 3. Logout Process
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText("Logout")).perform(click());
        onView(withText("Log Out")).perform(click());

        // 4. Login Again (To prove persistence)
        onView(withId(R.id.usernameLoginEditText)).perform(typeText("admin1"), closeSoftKeyboard());
        onView(withId(R.id.passwordLoginEditText)).perform(typeText("admin1"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // 5. Verify the Log Exists
        // We check the screen to see if "Bench Press" is visible.
        onView(withText(exercise)).check(matches(isDisplayed()));
    }
}