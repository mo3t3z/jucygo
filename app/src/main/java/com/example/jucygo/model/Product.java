package com.example.jucygo.model;

/**
 * Model class representing a Product in the Juice Sales Management system.
 * Contains product attributes: id, name, price, quantity, description, and imagePath.
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String imagePath;

    // Constructor for creating a new product (without id)
    public Product(String name, double price, int quantity, String description) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description != null ? description : "";
        this.imagePath = "";
    }

    // Constructor for creating a new product with image (without id)
    public Product(String name, double price, int quantity, String description, String imagePath) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description != null ? description : "";
        this.imagePath = imagePath != null ? imagePath : "";
    }

    // Constructor for existing product (with id)
    public Product(int id, String name, double price, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description != null ? description : "";
        this.imagePath = "";
    }

    // Constructor for existing product with image (with id)
    public Product(int id, String name, double price, int quantity, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description != null ? description : "";
        this.imagePath = imagePath != null ? imagePath : "";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description : "";
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath != null ? imagePath : "";
    }

    public boolean hasImage() {
        return imagePath != null && !imagePath.isEmpty();
    }
}


