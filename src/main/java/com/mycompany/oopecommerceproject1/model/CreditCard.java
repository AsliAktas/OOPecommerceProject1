package com.mycompany.oopecommerceproject1.model;

/**
 * Credit card model class.
 * - userId: the ID of the user who owns the card
 * - cardNumber: the card number (stored as String)
 * - expiryMonth: expiration month
 * - expiryYear: expiration year
 * - cvv: card CVV code
 *
 * The toString() method determines what text appears in a ListView.
 * For security, only the last four digits of the card number are shown.
 */
public class CreditCard {
    private int userId;
    private String cardNumber;
    private int expiryMonth;
    private int expiryYear;
    private String cvv;

    public CreditCard(int userId, String cardNumber, int expiryMonth, int expiryYear, String cvv) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
    }

    // ───────── Getters / Setters ─────────
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }
    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public int getExpiryYear() {
        return expiryYear;
    }
    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCvv() {
        return cvv;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    /**
     * Overrides toString() to define what text is shown in a ListView.
     * - Only the last four digits of the card are shown for security
     * - e.g.: “**** **** **** 1234 (Exp: MM/YYYY)”
     */
    @Override
    public String toString() {
        String masked;
        if (cardNumber != null && cardNumber.length() >= 4) {
            String last4 = cardNumber.substring(cardNumber.length() - 4);
            masked = "**** **** **** " + last4;
        } else {
            // If card number is too short or invalid, show as-is
            masked = cardNumber;
        }
        return masked + "  (Exp: " + expiryMonth + "/" + expiryYear + ")";
    }
}
