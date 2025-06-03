package com.mycompany.oopecommerceproject1.model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int userId;
    private int cardId;
    private double totalAmount;
    private Timestamp orderDate;

    // Tam yapıcı (DB’den okuyup kaydederken)
    public Order(int id, int userId, int cardId, double totalAmount, Timestamp orderDate) {
        this.id = id;
        this.userId = userId;
        this.cardId = cardId;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    // Yeni sipariş oluşturmak için yapıcı (id, orderDate DB’de atanacak)
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

