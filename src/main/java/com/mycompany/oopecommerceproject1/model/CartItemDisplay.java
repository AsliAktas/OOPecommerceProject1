package com.mycompany.oopecommerceproject1.model;

/**
 * CartItemDisplay is a DTO used in the Cart table.
 * It holds:
 *   - cartItemId (the primary key from cart_items table)
 *   - productName (for display)
 *   - quantity (cart quantity)
 */
public class CartItemDisplay {
    private int cartItemId;
    private String productName;
    private int quantity;

    public CartItemDisplay(int cartItemId, String productName, int quantity) {
        this.cartItemId = cartItemId;
        this.productName = productName;
        this.quantity = quantity;
    }

    public int getCartItemId() {
        return cartItemId;
    }
    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
