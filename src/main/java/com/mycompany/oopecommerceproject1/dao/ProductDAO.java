package com.mycompany.oopecommerceproject1.dao;

import com.mycompany.oopecommerceproject1.model.Product;
import com.mycompany.oopecommerceproject1.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for the products table:
 * - getAllProducts: fetches all products
 * - getProductById: fetches a single product by ID
 * - insertProduct: inserts a new product (and sets its generated ID)
 * - updateProduct: updates an existing product
 * - deleteProductById: deletes a product by ID
 */
public class ProductDAO {

    /**
     * Returns a list of all products from the database.
     * @return List<Product> (empty if no products)
     */
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, stock FROM products";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );
                products.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    /**
     * Returns the product with the specified ID.
     * @param productId The product’s ID
     * @return A Product object or null if not found
     */
    public static Product getProductById(int productId) {
        String sql = "SELECT id, name, description, price, stock FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Inserts a new product into the database.
     * Sets the generated ID back into the Product object.
     * @param product Product object (name, description, price, stock must be set)
     * @return true if insertion succeeds, false otherwise
     */
    public static boolean insertProduct(Product product) {
        String sql = "INSERT INTO products (name, description, price, stock) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) return false;

            // Assign generated ID back to the object
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
            }
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing product.
     * @param product Product object (id, name, description, price, stock must be set)
     * @return true if update succeeds, false otherwise
     */
    public static boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setInt(5, product.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes the product with the specified ID.
     * @param productId The product’s ID to delete
     * @return true if deletion succeeds, false otherwise
     */
    public static boolean deleteProductById(int productId) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
