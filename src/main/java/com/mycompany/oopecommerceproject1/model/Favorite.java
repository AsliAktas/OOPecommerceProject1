
package com.mycompany.oopecommerceproject1.model;

/**
 * Model class representing a user’s favorite product.
 * - id: primary key from the favorites table
 * - userId: ID of the user who favorited the product
 * - productId: ID of the product that was favorited
 */
public class Favorite {
    private int id;
    private int userId;
    private int productId;

    /** Full constructor: used when reading from the database */
    public Favorite(int id, int userId, int productId) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
    }

    /** Constructor used when adding a new favorite (ID assigned later) */
    public Favorite(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    // ───────── Getters & Setters ─────────
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
}
