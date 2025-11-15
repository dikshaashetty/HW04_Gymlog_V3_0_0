package com.example.hw04_gymlog_v300;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.hw04_gymlog_v300.database.entities.User;

public class GymLogUnitTest {
    @Test
    public void user_admin_check() {
        // Create a user that IS an admin
        User adminUser = new User("admin1", "admin1");
        adminUser.setAdmin(true);

        // Create a user that is NOT an admin
        User regularUser = new User("testuser", "password");
        // default is false in your code

        // Assertions
        assertTrue("Admin user should return true", adminUser.isAdmin());
        assertFalse("Regular user should return false", regularUser.isAdmin());
    }

    @Test
    public void user_username_check() {
        User user = new User("CoolGuy", "securePass");
        assertEquals("CoolGuy", user.getUsername());
    }
}