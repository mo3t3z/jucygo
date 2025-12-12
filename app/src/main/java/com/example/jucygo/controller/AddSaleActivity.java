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
import com.example.jucygo.model.Product;
import com.example.jucygo.model.Sale;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity to record a new sale.
 * Handles product selection, quantity input, total calculation, and stock validation.
 */
public class AddSaleActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerProduct;
    private TextInputEditText editTextQuantitySold;
    private MaterialButton buttonConfirmSale;
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
        setContentView(R.layout.activity_add_sale);

        dbHelper = new DBHelper(this);
        priceFormat = new DecimalFormat("DT #,##0.00");

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
        spinnerProduct = findViewById(R.id.spinnerProduct);
        editTextQuantitySold = findViewById(R.id.editTextQuantitySold);
        buttonConfirmSale = findViewById(R.id.buttonConfirmSale);
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
            Toast.makeText(this, "No products available. Please add products first.", Toast.LENGTH_LONG).show();
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
            editTextQuantitySold.setText("");
            calculateTotal();
        }
    }

    /**
     * Setup listener for quantity input to calculate total dynamically.
     */
    private void setupQuantityListener() {
        editTextQuantitySold.addTextChangedListener(new TextWatcher() {
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

        String quantityStr = editTextQuantitySold.getText().toString().trim();
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
        buttonConfirmSale.setOnClickListener(v -> confirmSale());
        buttonCancel.setOnClickListener(v -> finish());
    }

    /**
     * Validate and confirm the sale.
     */
    private void confirmSale() {
        // Validate product selection
        if (selectedProduct == null) {
            Toast.makeText(this, "Please select a product", Toast.LENGTH_SHORT).show();
            spinnerProduct.requestFocus();
            return;
        }

        // Validate quantity
        String quantityStr = editTextQuantitySold.getText().toString().trim();
        if (TextUtils.isEmpty(quantityStr)) {
            editTextQuantitySold.setError("Quantity is required");
            editTextQuantitySold.requestFocus();
            return;
        }

        int quantitySold;
        try {
            quantitySold = Integer.parseInt(quantityStr);
            if (quantitySold <= 0) {
                editTextQuantitySold.setError("Quantity must be greater than 0");
                editTextQuantitySold.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextQuantitySold.setError("Invalid quantity format");
            editTextQuantitySold.requestFocus();
            return;
        }

        // Check stock availability
        if (!dbHelper.isStockSufficient(selectedProduct.getName(), quantitySold)) {
            Toast.makeText(this, "Insufficient stock. Available: " + selectedProduct.getQuantity(), Toast.LENGTH_LONG).show();
            editTextQuantitySold.setError("Insufficient stock");
            editTextQuantitySold.requestFocus();
            return;
        }

        // Calculate total amount
        double totalAmount = selectedProduct.getPrice() * quantitySold;

        // Create and save sale
        Sale sale = new Sale(
                selectedProduct.getName(),
                quantitySold,
                selectedProduct.getPrice(),
                totalAmount
        );

        long saleResult = dbHelper.addSale(sale);
        
        if (saleResult > 0) {
            // Update stock
            int updateResult = dbHelper.updateStockAfterSale(selectedProduct.getName(), quantitySold);
            
            if (updateResult > 0) {
                String message = "Sale recorded successfully!\nTotal: " + priceFormat.format(totalAmount);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Sale recorded but failed to update stock", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to record sale", Toast.LENGTH_SHORT).show();
        }
    }
}

