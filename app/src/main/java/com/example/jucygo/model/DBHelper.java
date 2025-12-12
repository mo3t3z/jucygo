package com.example.jucygo.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Helper class extending SQLiteOpenHelper.
 * Manages all CRUD operations for products in the SQLite database.
 */
public class DBHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "JucygoDB";
    private static final int DATABASE_VERSION = 4;

    // Table names
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_SALES = "sales";
    private static final String TABLE_ORDERS = "orders";

    // Product column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_PATH = "imagePath";

    // Sale column names
    private static final String COLUMN_SALE_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "productName";
    private static final String COLUMN_QUANTITY_SOLD = "quantitySold";
    private static final String COLUMN_UNIT_PRICE = "unitPrice";
    private static final String COLUMN_TOTAL_AMOUNT = "totalAmount";
    private static final String COLUMN_DATE = "date";

    // Order column names
    private static final String COLUMN_ORDER_ID = "id";
    private static final String COLUMN_CUSTOMER_NAME = "customerName";
    private static final String COLUMN_ORDER_PRODUCT_NAME = "productName";
    private static final String COLUMN_QUANTITY_ORDERED = "quantityOrdered";
    private static final String COLUMN_ORDER_UNIT_PRICE = "unitPrice";
    private static final String COLUMN_ORDER_TOTAL_AMOUNT = "totalAmount";
    private static final String COLUMN_ORDER_STATUS = "status";
    private static final String COLUMN_ORDER_DATE = "date";

    // SQL query to create the products table
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT NOT NULL,"
            + COLUMN_PRICE + " REAL NOT NULL,"
            + COLUMN_QUANTITY + " INTEGER NOT NULL,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_IMAGE_PATH + " TEXT"
            + ")";

    // SQL query to create the sales table
    private static final String CREATE_TABLE_SALES = "CREATE TABLE " + TABLE_SALES + "("
            + COLUMN_SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_PRODUCT_NAME + " TEXT NOT NULL,"
            + COLUMN_QUANTITY_SOLD + " INTEGER NOT NULL,"
            + COLUMN_UNIT_PRICE + " REAL NOT NULL,"
            + COLUMN_TOTAL_AMOUNT + " REAL NOT NULL,"
            + COLUMN_DATE + " TEXT NOT NULL"
            + ")";

    // SQL query to create the orders table
    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
            + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CUSTOMER_NAME + " TEXT NOT NULL,"
            + COLUMN_ORDER_PRODUCT_NAME + " TEXT NOT NULL,"
            + COLUMN_QUANTITY_ORDERED + " INTEGER NOT NULL,"
            + COLUMN_ORDER_UNIT_PRICE + " REAL NOT NULL,"
            + COLUMN_ORDER_TOTAL_AMOUNT + " REAL NOT NULL,"
            + COLUMN_ORDER_STATUS + " TEXT NOT NULL,"
            + COLUMN_ORDER_DATE + " TEXT NOT NULL"
            + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_SALES);
        db.execSQL(CREATE_TABLE_ORDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create sales table for version 2
            db.execSQL(CREATE_TABLE_SALES);
        }
        if (oldVersion < 3) {
            // Create orders table for version 3
            db.execSQL(CREATE_TABLE_ORDERS);
        }
        if (oldVersion < 4) {
            // Add imagePath column to products table for version 4
            db.execSQL("ALTER TABLE " + TABLE_PRODUCTS + " ADD COLUMN " + COLUMN_IMAGE_PATH + " TEXT");
        }
    }

    /**
     * Add a new product to the database.
     * @param product The product to add
     * @return The row ID of the newly inserted row, or -1 if an error occurred
     */
    public long addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_IMAGE_PATH, product.getImagePath());

        long result = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return result;
    }

    /**
     * Retrieve all products from the database.
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productList;
    }

    /**
     * Retrieve a product by its ID from the database.
     * @param id The id of the product to retrieve
     * @return The product if found, null otherwise
     */
    public Product getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_ID + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        Product product = null;
        if (cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH))
            );
        }

        cursor.close();
        db.close();
        return product;
    }

    /**
     * Update an existing product in the database.
     * @param product The product to update (must have a valid id)
     * @return The number of rows affected
     */
    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_PRICE, product.getPrice());
        values.put(COLUMN_QUANTITY, product.getQuantity());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        values.put(COLUMN_IMAGE_PATH, product.getImagePath());

        int result = db.update(TABLE_PRODUCTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        db.close();
        return result;
    }

    /**
     * Delete a product from the database.
     * @param id The id of the product to delete
     * @return The number of rows affected
     */
    public int deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    /**
     * Get a product by name.
     * @param productName The name of the product
     * @return The product if found, null otherwise
     */
    public Product getProductByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_NAME + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{productName});

        Product product = null;
        if (cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH))
            );
        }

        cursor.close();
        db.close();
        return product;
    }

    /**
     * Check if a product has sufficient stock for a sale.
     * @param productName The name of the product
     * @param quantityRequested The quantity to be sold
     * @return true if stock is sufficient, false otherwise
     */
    public boolean isStockSufficient(String productName, int quantityRequested) {
        Product product = getProductByName(productName);
        if (product == null) {
            return false;
        }
        return product.getQuantity() >= quantityRequested;
    }

    /**
     * Update product stock after a sale.
     * @param productName The name of the product
     * @param quantitySold The quantity sold
     * @return The number of rows affected
     */
    public int updateStockAfterSale(String productName, int quantitySold) {
        Product product = getProductByName(productName);
        if (product == null) {
            return 0;
        }

        int newQuantity = product.getQuantity() - quantitySold;
        if (newQuantity < 0) {
            return 0; // Prevent negative stock
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);

        int result = db.update(TABLE_PRODUCTS, values, COLUMN_NAME + " = ?",
                new String[]{productName});
        db.close();
        return result;
    }

    /**
     * Add a new sale to the database.
     * @param sale The sale to add
     * @return The row ID of the newly inserted row, or -1 if an error occurred
     */
    public long addSale(Sale sale) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PRODUCT_NAME, sale.getProductName());
        values.put(COLUMN_QUANTITY_SOLD, sale.getQuantitySold());
        values.put(COLUMN_UNIT_PRICE, sale.getUnitPrice());
        values.put(COLUMN_TOTAL_AMOUNT, sale.getTotalAmount());
        values.put(COLUMN_DATE, sale.getDate());

        long result = db.insert(TABLE_SALES, null, values);
        db.close();
        return result;
    }

    /**
     * Retrieve all sales from the database.
     * @return List of all sales
     */
    public List<Sale> getAllSales() {
        List<Sale> saleList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SALES + " ORDER BY " + COLUMN_SALE_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sale sale = new Sale(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SALE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_SOLD)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNIT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                );
                saleList.add(sale);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return saleList;
    }

    /**
     * Search sales by product name (case-insensitive partial match).
     * @param query The search query (product name)
     * @return List of matching sales
     */
    public List<Sale> searchSalesByProductName(String query) {
        List<Sale> saleList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SALES + 
                " WHERE " + COLUMN_PRODUCT_NAME + " LIKE ?" +
                " ORDER BY " + COLUMN_SALE_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                Sale sale = new Sale(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SALE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_SOLD)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNIT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                );
                saleList.add(sale);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return saleList;
    }

    /**
     * Search sales by date (matches date part, not time).
     * @param dateQuery The date string to search (e.g., "2024-01-15")
     * @return List of matching sales
     */
    public List<Sale> searchSalesByDate(String dateQuery) {
        List<Sale> saleList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SALES + 
                " WHERE " + COLUMN_DATE + " LIKE ?" +
                " ORDER BY " + COLUMN_SALE_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{dateQuery + "%"});

        if (cursor.moveToFirst()) {
            do {
                Sale sale = new Sale(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SALE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_SOLD)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNIT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                );
                saleList.add(sale);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return saleList;
    }

    /**
     * Get total sales amount for a specific date.
     * @param date The date string (e.g., "2024-01-15")
     * @return Total amount for that date
     */
    public double getTotalSalesByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_TOTAL_AMOUNT + ") FROM " + TABLE_SALES +
                " WHERE " + COLUMN_DATE + " LIKE ?";
        
        Cursor cursor = db.rawQuery(query, new String[]{date + "%"});
        
        double total = 0.0;
        if (cursor.moveToFirst() && !cursor.isNull(0)) {
            total = cursor.getDouble(0);
        }
        
        cursor.close();
        db.close();
        return total;
    }

    // ==================== ORDER MANAGEMENT ====================

    /**
     * Add a new order to the database and update stock.
     * @param order The order to add
     * @return The row ID of the newly inserted row, or -1 if an error occurred
     */
    public long addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_CUSTOMER_NAME, order.getCustomerName());
        values.put(COLUMN_ORDER_PRODUCT_NAME, order.getProductName());
        values.put(COLUMN_QUANTITY_ORDERED, order.getQuantityOrdered());
        values.put(COLUMN_ORDER_UNIT_PRICE, order.getUnitPrice());
        values.put(COLUMN_ORDER_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COLUMN_ORDER_STATUS, order.getStatus());
        values.put(COLUMN_ORDER_DATE, order.getDate());

        long result = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return result;
    }

    /**
     * Retrieve all orders from the database.
     * @return List of all orders
     */
    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " ORDER BY " + COLUMN_ORDER_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_ORDERED)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_UNIT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }

    /**
     * Retrieve an order by its ID from the database.
     * @param id The id of the order to retrieve
     * @return The order if found, null otherwise
     */
    public Order getOrderById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_ORDER_ID + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});

        Order order = null;
        if (cursor.moveToFirst()) {
            order = new Order(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_ORDERED)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_UNIT_PRICE)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE))
            );
        }

        cursor.close();
        db.close();
        return order;
    }

    /**
     * Update the status of an order.
     * @param orderId The id of the order
     * @param newStatus The new status ("pending", "completed", "cancelled")
     * @return The number of rows affected
     */
    public int updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_STATUS, newStatus);

        int result = db.update(TABLE_ORDERS, values, COLUMN_ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)});
        db.close();
        return result;
    }

    /**
     * Delete an order from the database.
     * @param id The id of the order to delete
     * @return The number of rows affected
     */
    public int deleteOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ORDERS, COLUMN_ORDER_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    /**
     * Search orders by customer name (case-insensitive partial match).
     * @param query The search query (customer name)
     * @return List of matching orders
     */
    public List<Order> searchOrdersByCustomerName(String query) {
        List<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + 
                " WHERE " + COLUMN_CUSTOMER_NAME + " LIKE ?" +
                " ORDER BY " + COLUMN_ORDER_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_ORDERED)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_UNIT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }

    /**
     * Search orders by product name (case-insensitive partial match).
     * @param query The search query (product name)
     * @return List of matching orders
     */
    public List<Order> searchOrdersByProductName(String query) {
        List<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + 
                " WHERE " + COLUMN_ORDER_PRODUCT_NAME + " LIKE ?" +
                " ORDER BY " + COLUMN_ORDER_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_ORDERED)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_UNIT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }

    /**
     * Search orders by date (matches date part, not time).
     * @param dateQuery The date string to search (e.g., "2024-01-15")
     * @return List of matching orders
     */
    public List<Order> searchOrdersByDate(String dateQuery) {
        List<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + 
                " WHERE " + COLUMN_ORDER_DATE + " LIKE ?" +
                " ORDER BY " + COLUMN_ORDER_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{dateQuery + "%"});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_ORDERED)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_UNIT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }

    /**
     * Get pending orders only.
     * @return List of pending orders
     */
    public List<Order> getPendingOrders() {
        List<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + 
                " WHERE " + COLUMN_ORDER_STATUS + " = ?" +
                " ORDER BY " + COLUMN_ORDER_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{Order.STATUS_PENDING});

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY_ORDERED)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_UNIT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }
}

