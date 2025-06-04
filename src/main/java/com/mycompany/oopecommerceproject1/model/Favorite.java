
package com.mycompany.oopecommerceproject1.model;

/**
 * Kullanıcının favori ürünlerini temsil eden model.
 */
public class Favorite {
    private int id;
    private int userId;
    private int productId;

    // DB’den okurken kullanılan tam yapıcı
    public Favorite(int id, int userId, int productId) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
    }

    // Yeni favori eklerken kullanılan yapıcı (id, sonraki sorgularda atanacak)
    public Favorite(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
}
