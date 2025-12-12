package com.example.jucygo.controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jucygo.R;
import com.example.jucygo.model.DBHelper;
import com.example.jucygo.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Activity to add a new product to the database.
 * Validates input fields before saving.
 */
public class AddProductActivity extends AppCompatActivity {

    private TextInputEditText editTextProductName;
    private TextInputEditText editTextProductPrice;
    private TextInputEditText editTextProductQuantity;
    private TextInputEditText editTextProductDescription;
    private MaterialButton buttonAddProduct;
    private MaterialButton buttonCancel;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = new DBHelper(this);

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextProductQuantity = findViewById(R.id.editTextProductQuantity);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        buttonAddProduct = findViewById(R.id.buttonAddProduct);
        buttonCancel = findViewById(R.id.buttonCancel);

        buttonAddProduct.setOnClickListener(v -> addProduct());
        buttonCancel.setOnClickListener(v -> finish());
    }

    /**
     * Validates and adds a new product to the database.
     */
    private void addProduct() {
        String name = editTextProductName.getText().toString().trim();
        String priceStr = editTextProductPrice.getText().toString().trim();
        String quantityStr = editTextProductQuantity.getText().toString().trim();
        String description = editTextProductDescription.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(name)) {
            editTextProductName.setError("Product name is required");
            editTextProductName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(priceStr)) {
            editTextProductPrice.setError("Product price is required");
            editTextProductPrice.requestFocus();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                editTextProductPrice.setError("Price must be positive");
                editTextProductPrice.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextProductPrice.setError("Invalid price format");
            editTextProductPrice.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantityStr)) {
            editTextProductQuantity.setError("Product quantity is required");
            editTextProductQuantity.requestFocus();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity < 0) {
                editTextProductQuantity.setError("Quantity must be positive");
                editTextProductQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            editTextProductQuantity.setError("Invalid quantity format");
            editTextProductQuantity.requestFocus();
            return;
        }

        // Create product and add to database
        Product product = new Product(name, price, quantity, description);
        long result = dbHelper.addProduct(product);

        if (result > 0) {
            Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show();
        }
    }
}

