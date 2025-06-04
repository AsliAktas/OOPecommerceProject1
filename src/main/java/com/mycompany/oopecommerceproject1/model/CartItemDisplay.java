package com.mycompany.oopecommerceproject1.model;

/**
 * DTO used in the Cart TableView:
 * - cartItemId: the primary key from the cart_items table
 * - productName: for display
 * - quantity: the quantity in the cart
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
