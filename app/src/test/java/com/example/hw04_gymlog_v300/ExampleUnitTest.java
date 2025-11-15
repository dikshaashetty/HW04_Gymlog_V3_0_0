package com.example.hw04_gymlog_v300;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.hw04_gymlog_v300.database.entities.User;

/**
 * Local Unit Test.
 * this is used to verify pure Java logic in isolation, ensuring our User entity behaves as expected
 * before we try to use it in the complex Android environment.
 */
public class ExampleUnitTest {
    @Test
    public void user_isAdmin_check() {
        // 1. Creating a user object acting as an Admin
        // We use the 2-argument constructor (username, password) defined in our Entity.
        User admin = new User("admin1", "admin1");

        // 2. Manually set the admin privileges to true
        admin.setAdmin(true);

        // 3. Assertion: Check if the getter correctly reports the user as an admin
        // If this fails, it means the boolean logic in the User class is broken
        assertTrue("Admin user should return true for isAdmin()", admin.isAdmin());

        // 4. Create a standard user
        User regular = new User("test", "test");

        // 5. Assertion: Check defaults
        // A new user should not be an admin by default
        assertFalse("Regular user should return false for isAdmin()", regular.isAdmin());
    }
}