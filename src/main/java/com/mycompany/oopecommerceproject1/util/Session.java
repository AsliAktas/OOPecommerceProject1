package com.mycompany.oopecommerceproject1.util;

public class Session {
    private static String currentUsername;

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }
}
