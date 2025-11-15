package com.example.hw04_gymlog_v300;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.hw04_gymlog_v300.database.AppDataBase;
import com.example.hw04_gymlog_v300.database.entities.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class GymLogEspressoTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    // This runs before the test to reset the database
    @Before
    public void clearDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        AppDataBase db = AppDataBase.getDatabase(context);

        // 1. Wipe the database to start fresh
        db.clearAllTables();

        // 2. Create the Admin User so we can log in
        User admin = new User("admin1", "admin1");
        admin.setAdmin(true);
        db.userDAO().insert(admin);
    }

    @Test
    public void loginAddLogAndVerify() throws InterruptedException {
        // 1. FIRST LOGIN
        try {
            onView(withId(R.id.usernameLoginEditText)).perform(typeText("admin1"), closeSoftKeyboard());
            onView(withId(R.id.passwordLoginEditText)).perform(typeText("admin1"), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(click());
            Thread.sleep(3000);
        } catch (Exception e) {
        }

        // 2. ADD A LOG
        String exercise = "Bench Press";
        onView(withId(R.id.exerciseInputEditText)).perform(typeText(exercise), closeSoftKeyboard());
        onView(withId(R.id.weightInputEditText)).perform(typeText("150"), closeSoftKeyboard());
        onView(withId(R.id.repInputEditText)).perform(typeText("5"), closeSoftKeyboard());
        onView(withId(R.id.logButton)).perform(click());

        // 3. FIRST LOGOUT
        onView(withId(R.id.log_out_menu_item)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        // 4. SECOND LOGIN
        onView(withId(R.id.usernameLoginEditText)).perform(typeText("admin1"), closeSoftKeyboard());
        onView(withId(R.id.passwordLoginEditText)).perform(typeText("admin1"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // 5. VERIFY LOG IS STILL THERE
        onView(withText(containsString(exercise))).check(matches(isDisplayed()));
        Thread.sleep(5000);

        // 7. FINAL LOGOUT
        onView(withId(R.id.log_out_menu_item)).perform(click());
        Thread.sleep(3000);
        onView(withId(android.R.id.button1)).perform(click());

        // 8. Confirm we are back at Login screen
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }
}