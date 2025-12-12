package com.example.jucygo.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Model class representing a Sale in the Juice Sales Management system.
 * Contains sale attributes: id, productName, quantitySold, unitPrice, totalAmount, and date.
 */
public class Sale {
    private int id;
    private String productName;
    private int quantitySold;
    private double unitPrice;
    private double totalAmount;
    private String date;

    // Constructor for creating a new sale (without id, date auto-generated)
    public Sale(String productName, int quantitySold, double unitPrice, double totalAmount) {
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.date = generateCurrentDate();
    }

    // Constructor for existing sale (with id and date)
    public Sale(int id, String productName, int quantitySold, double unitPrice, double totalAmount, String date) {
        this.id = id;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

