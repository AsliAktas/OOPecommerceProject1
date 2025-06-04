package com.mycompany.oopecommerceproject1.model;

/**
 * Model class for a User.
 * - id: primary key from the users table
 * - username: user’s username
 * - email: user’s email address
 * - password: user’s password (in a real application, store as a salted hash)
 *
 * Includes a no-argument constructor and a full-field constructor.
 */
public class User {
    private int id;
    private String username;
    private String email;
    private String password;

    public User() { }

    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // ───────── Getters / Setters ─────────
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Note: In production, store the password as a salted-hash.
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
