package com.example.jucygo.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jucygo.R;
import com.example.jucygo.model.DBHelper;
import com.example.jucygo.model.Product;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Activity to edit or delete an existing product.
 * Validates input fields before updating.
 */
public class EditProductActivity extends AppCompatActivity {

    private TextInputEditText editTextProductName;
    private TextInputEditText editTextProductPrice;
    private TextInputEditText editTextProductQuantity;
    private TextInputEditText editTextProductDescription;
    private MaterialButton buttonUpdateProduct;
    private MaterialButton buttonDeleteProduct;
    private MaterialButton buttonCancel;
    private DBHelper dbHelper;
    private int productId;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        dbHelper = new DBHelper(this);
        productId = getIntent().getIntExtra("product_id", -1);

        if (productId == -1) {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextProductQuantity = findViewById(R.id.editTextProductQuantity);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        buttonUpdateProduct = findViewById(R.id.buttonUpdateProduct);
        buttonDeleteProduct = findViewById(R.id.buttonDeleteProduct);
        buttonCancel = findViewById(R.id.buttonCancel);

        loadProduct();

        buttonUpdateProduct.setOnClickListener(v -> updateProduct());
        buttonDeleteProduct.setOnClickListener(v -> showDeleteConfirmationDialog());
        buttonCancel.setOnClickListener(v -> finish());
    }

    /**
     * Loads the product data from database and populates the form fields.
     */
    private void loadProduct() {
        currentProduct = dbHelper.getProductById(productId);

        if (currentProduct == null) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editTextProductName.setText(currentProduct.getName());
        editTextProductPrice.setText(String.valueOf(currentProduct.getPrice()));
        editTextProductQuantity.setText(String.valueOf(currentProduct.getQuantity()));
        editTextProductDescription.setText(currentProduct.getDescription());
    }

    /**
     * Validates and updates the product in the database.
     */
    private void updateProduct() {
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

        // Update product in database
        Product updatedProduct = new Product(productId, name, price, quantity, description);
        int result = dbHelper.updateProduct(updatedProduct);

        if (result > 0) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Shows a confirmation dialog before deleting the product.
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure you want to delete \"" + currentProduct.getName() + "\"?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Deletes the product from the database.
     */
    private void deleteProduct() {
        int result = dbHelper.deleteProduct(productId);

        if (result > 0) {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
        }
    }
}

