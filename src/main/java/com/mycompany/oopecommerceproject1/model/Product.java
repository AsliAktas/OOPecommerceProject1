package com.mycompany.oopecommerceproject1.model;

/**
 * Model class for a Product.
 * - id: primary key from the products table
 * - name: product name
 * - description: product description
 * - price: unit price
 * - stock: current stock quantity
 *
 * Plain getters and setters for each field.
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;

    public Product(int id, String name, String description, double price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    // ───────── Getters & Setters ─────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
