package com.example.jucygo.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jucygo.R;
import com.example.jucygo.model.DBHelper;
import com.example.jucygo.model.Order;
import com.example.jucygo.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity to create a new order (commande) for a customer.
 * The vendor manually places the order and stock is updated immediately.
 */
public class AddOrderActivity extends AppCompatActivity {

    private TextInputEditText editTextCustomerName;
    private AutoCompleteTextView spinnerProduct;
    private TextInputEditText editTextQuantityOrdered;
    private MaterialButton buttonConfirmOrder;
    private MaterialButton buttonCancel;
    private android.widget.TextView textViewCurrentStock;
    private android.widget.TextView textViewUnitPrice;
    private android.widget.TextView textViewTotalAmount;

    private DBHelper dbHelper;
    private List<Product> productList;
    private Product selectedProduct;
    private ArrayAdapter<String> productAdapter;
    private DecimalFormat priceFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        dbHelper = new DBHelper(this);
        priceFormat = new DecimalFormat("$#,##0.00");

        initializeViews();
        loadProducts();
        setupProductSpinner();
        setupQuantityListener();
        setupButtons();
    }

    /**
     * Initialize all view components.
     */
    private void initializeViews() {
        editTextCustomerName = findViewById(R.id.editTextCustomerName);
        spinnerProduct = findViewById(R.id.spinnerProduct);
        editTextQuantityOrdered = findViewById(R.id.editTextQuantityOrdered);
        buttonConfirmOrder = findViewById(R.id.buttonConfirmOrder);
        buttonCancel = findViewById(R.id.buttonCancel);
        textViewCurrentStock = findViewById(R.id.textViewCurrentStock);
        textViewUnitPrice = findViewById(R.id.textViewUnitPrice);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
    }

    /**
     * Load all products from database.
     */
    private void loadProducts() {
        productList = dbHelper.getAllProducts();
        
        if (productList.isEmpty()) {
            Toast.makeText(this, R.string.no_products_for_order, Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }

    /**
     * Setup the product spinner with product names.
     */
    private void setupProductSpinner() {
        List<String> productNames = new ArrayList<>();
        for (Product product : productList) {
            productNames.add(product.getName());
        }

        productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, productNames);
        spinnerProduct.setAdapter(productAdapter);

        spinnerProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedProductName = (String) parent.getItemAtPosition(position);
                selectProduct(selectedProductName);
            }
        });
    }

    /**
     * Select a product and update the display.
     */
    private void selectProduct(String productName) {
        selectedProduct = dbHelper.getProductByName(productName);
        
        if (selectedProduct != null) {
            textViewCurrentStock.setText(String.valueOf(selectedProduct.getQuantity()));
            textViewUnitPrice.setText(priceFormat.format(selectedProduct.getPrice()));
            
            // Clear quantity and recalculate total
            editTextQuantityOrdered.setText("");
            calculateTotal();
        }
    }

    /**
     * Setup listener for quantity input to calculate total dynamically.
     */
    private void setupQuantityListener() {
        editTextQuantityOrdered.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotal();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Calculate and display the total amount based on selected product and quantity.
     */
    private void calculateTotal() {
        if (selectedProduct == null) {
            textViewTotalAmount.setText("$0.00");
            return;
        }

        String quantityStr = editTextQuantityOrdered.getText().toString().trim();
        if (TextUtils.isEmpty(quantityStr)) {
            textViewTotalAmount.setText("$0.00");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                textViewTotalAmount.setText("$0.00");
                return;
            }

            double total = selectedProduct.getPrice() * quantity;
            textViewTotalAmount.setText(priceFormat.format(total));
        } catch (NumberFormatException e) {
            textViewTotalAmount.setText("$0.00");
        }
    }

    /**
     * Setup button click listeners.
     */
    private void setupButtons() {
        buttonConfirmOrder.setOnClickListener(v -> confirmOrder());
        buttonCancel.setOnClickListener(v -> finish());
    }

    /**
     * Validate and confirm the order.
     */
    private void confirmOrder() {
        // Validate customer name
        String customerName = editTextCustomerName.getText().toString().trim();
        if (TextUtils.isEmpty(customerName)) {
            editTextCustomerName.setError(getString(R.string.customer_name_required));
            editTextCustomerName.requestFocus();
            return;
        }

        // Validate product selection
        if (selectedProduct == null) {
            Toast.makeText(this, R.string.select_product_message, Toast.LENGTH_SHORT).show();
            spinnerProduct.requestFocus();
            return;
        }

        // Validate quantity
        String quantityStr = editTextQuantityOrdered.getText().toString().trim();
        if (TextUtils.isEmpty(quantityStr)) {
            editTextQuantityOrdered.setError(getString(R.string.quantity_required));
            editTextQuantityOrdered.requestFocus();
            return;
        }

        int quantityOrdered;
        try {
            quantityOrdered = Integer.parseInt(quantityStr);
            if (quantityOrdered <= 0) {
                editTextQuantityOrdered.setError(getString(R.string.quantity_greater_than_zero));
                editTextQuantityOrdered.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextQuantityOrdered.setError(getString(R.string.invalid_quantity_format));
            editTextQuantityOrdered.requestFocus();
            return;
        }

        // Check stock availability
        if (!dbHelper.isStockSufficient(selectedProduct.getName(), quantityOrdered)) {
            Toast.makeText(this, getString(R.string.insufficient_stock_order) + selectedProduct.getQuantity(), 
                    Toast.LENGTH_LONG).show();
            editTextQuantityOrdered.setError(getString(R.string.insufficient_stock));
            editTextQuantityOrdered.requestFocus();
            return;
        }

        // Calculate total amount
        double totalAmount = selectedProduct.getPrice() * quantityOrdered;

        // Create and save order
        Order order = new Order(
                customerName,
                selectedProduct.getName(),
                quantityOrdered,
                selectedProduct.getPrice(),
                totalAmount
        );

        long orderResult = dbHelper.addOrder(order);
        
        if (orderResult > 0) {
            // Update stock immediately when order is placed
            int updateResult = dbHelper.updateStockAfterSale(selectedProduct.getName(), quantityOrdered);
            
            if (updateResult > 0) {
                String message = getString(R.string.order_success) + "\n" +
                        getString(R.string.customer_label) + " " + customerName + "\n" +
                        getString(R.string.total_label) + " " + priceFormat.format(totalAmount);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, R.string.order_stock_update_failed, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.order_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
