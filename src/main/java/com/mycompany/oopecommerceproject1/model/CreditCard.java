package com.mycompany.oopecommerceproject1.model;

public class CreditCard {
    private int id;
    private int userId;
    private String cardNumber;
    private int expiryMonth;
    private int expiryYear;
    private String cvv;

    // ID'siz constructor (INSERT işlemleri için)
    public CreditCard(int userId, String cardNumber, int expiryMonth, int expiryYear, String cvv) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
    }

    // Tüm alanlı constructor (SELECT işlemleri için)
    public CreditCard(int id, int userId, String cardNumber, int expiryMonth, int expiryYear, String cvv) {
        this.id = id;
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
    }

    // Getter / Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public int getExpiryMonth() { return expiryMonth; }
    public void setExpiryMonth(int expiryMonth) { this.expiryMonth = expiryMonth; }

    public int getExpiryYear() { return expiryYear; }
    public void setExpiryYear(int expiryYear) { this.expiryYear = expiryYear; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
}
