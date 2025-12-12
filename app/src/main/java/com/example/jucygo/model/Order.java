package com.example.jucygo.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Model class representing an Order (Commande) in the Juice Sales Management system.
 * An order is a manual command placed by the vendor for a customer.
 * Contains order attributes: id, customerName, productName, quantityOrdered, unitPrice, totalAmount, status, and date.
 */
public class Order {
    private int id;
    private String customerName;
    private String productName;
    private int quantityOrdered;
    private double unitPrice;
    private double totalAmount;
    private String status; // "pending", "completed", "cancelled"
    private String date;

    // Status constants
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELLED = "cancelled";

    // Constructor for creating a new order (without id, date auto-generated)
    public Order(String customerName, String productName, int quantityOrdered, double unitPrice, double totalAmount) {
        this.customerName = customerName;
        this.productName = productName;
        this.quantityOrdered = quantityOrdered;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.status = STATUS_PENDING;
        this.date = generateCurrentDate();
    }

    // Constructor for existing order (with id and date)
    public Order(int id, String customerName, String productName, int quantityOrdered, 
                 double unitPrice, double totalAmount, String status, String date) {
        this.id = id;
        this.customerName = customerName;
        this.productName = productName;
        this.quantityOrdered = quantityOrdered;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.status = status;
        this.date = date;
    }

    /**
     * Generates the current date and time in a readable format.
     * @return Formatted date string
     */
    private String generateCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Check if the order is pending.
     * @return true if pending, false otherwise
     */
    public boolean isPending() {
        return STATUS_PENDING.equals(status);
    }

    /**
     * Check if the order is completed.
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return STATUS_COMPLETED.equals(status);
    }

    /**
     * Check if the order is cancelled.
     * @return true if cancelled, false otherwise
     */
    public boolean isCancelled() {
        return STATUS_CANCELLED.equals(status);
    }
}
