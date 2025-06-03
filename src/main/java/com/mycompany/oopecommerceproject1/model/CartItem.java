package com.mycompany.oopecommerceproject1.model;

import java.sql.Timestamp;

/**
 * Represents a row in the cart_items table:
 *   id           INT AUTO_INCREMENT PRIMARY KEY
 *   user_id      INT NOT NULL            -- references users(id)
 *   product_id   INT NOT NULL            -- references products(id)
 *   quantity     INT NOT NULL DEFAULT 1
 *   added_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
 */
public class CartItem {
    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private Timestamp addedAt;

    /** Full constructor (used when reading from DB) */
    public CartItem(int id, int userId, int productId, int quantity, Timestamp addedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.addedAt = addedAt;
    }

    /** Partial constructor (used when inserting a brand‐new cart row) */
    public CartItem(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // ───────────────────────────────────────────────────────────────────────────
    // Getters & Setters
    // ───────────────────────────────────────────────────────────────────────────

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }
    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }
}
