package com.mycompany.oopecommerceproject1.model;

import java.sql.Timestamp;

/**
 * Model class for an Order.
 * - id: primary key from the orders table
 * - userId: ID of the user who placed the order
 * - cardId: ID of the credit card used
 * - totalAmount: total amount of the order
 * - orderDate: timestamp when the order was placed
 *
 * Two constructors:
 * 1) Full constructor: used when reading from the DB (id and orderDate provided)
 * 2) Constructor for new orders: id and orderDate are assigned by the DB
 */
public class Order {
    private int id;
    private int userId;
    private int cardId;
    private double totalAmount;
    private Timestamp orderDate;

    /** Full constructor (used when reading from DB) */
    public Order(int id, int userId, int cardId, double totalAmount, Timestamp orderDate) {
        this.id = id;
        this.userId = userId;
        this.cardId = cardId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    /** Constructor for creating a new order (id and orderDate assigned by DB) */
    public Order(int userId, int cardId, double totalAmount) {
        this.userId = userId;
        this.cardId = cardId;
        this.totalAmount = totalAmount;
    }

    // ───────── Getters & Setters ─────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCardId() { return cardId; }
    public void setCardId(int cardId) { this.cardId = cardId; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
}
