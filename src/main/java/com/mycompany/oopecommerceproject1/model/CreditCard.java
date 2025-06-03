package com.mycompany.oopecommerceproject1.model;

/**
 * Kredi kartı model sınıfı.
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

    // ---------- Getters / Setters ----------
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
     * toString() metodunu ezerek ListView içinde görünecek metni belirliyoruz.
     * Örneğin:
     * - Tüm kart numarasını göstermek isterseniz: cardNumber
     * - Ya da yalnızca son dört haneyi göstermek isterseniz: "**** **** **** " + last4
     */
    @Override
    public String toString() {
        // 1) Tüm kart numarasını göstermek isterseniz:
        // return cardNumber + "  (Exp: " + expiryMonth + "/" + expiryYear + ")";

        // 2) Sadece son dört haneyi göstermek daha güvenli:
        String masked;
        if (cardNumber != null && cardNumber.length() >= 4) {
            String last4 = cardNumber.substring(cardNumber.length() - 4);
            masked = "**** **** **** " + last4;
        } else {
            masked = cardNumber; // Kart numarası kısa veya hatalıysa olduğu gibi yaz
        }
        return masked + "  (Exp: " + expiryMonth + "/" + expiryYear + ")";
    }
}
