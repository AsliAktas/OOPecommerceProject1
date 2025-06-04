package com.mycompany.oopecommerceproject1.model;

/**
 * Model class for an OrderItem.
 * - id: primary key from the order_items table
 * - orderId: the ID of the order this item belongs to
 * - productId: the product’s ID
 * - quantity: the quantity ordered
 * - unitPrice: the unit price of the product at the time of order
 *
 * Two constructors:
 * 1) Full constructor: used when reading from the DB (id provided)
 * 2) Constructor for inserting a new order item: id assigned by DB
 */
public class OrderItem {
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private double unitPrice;

    /** Full constructor (used when reading from DB) */
    public OrderItem(int id, int orderId, int productId, int quantity, double unitPrice) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /** Constructor for adding a new order item (id assigned by DB) */
    public OrderItem(int orderId, int productId, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // ───────── Getters & Setters ─────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
}
