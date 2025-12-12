package com.example.jucygo.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jucygo.R;
import com.example.jucygo.model.DBHelper;
import com.example.jucygo.model.Product;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Activity to display the list of all products using RecyclerView.
 * Allows navigation to AddProductActivity and EditProductActivity.
 */
public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private TextView textViewEmpty;
    private MaterialButton buttonAddNewProduct;
    private MaterialButton buttonRecordSale;
    private MaterialButton buttonSalesHistory;
    private ProductAdapter productAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        dbHelper = new DBHelper(this);

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        buttonAddNewProduct = findViewById(R.id.buttonAddNewProduct);
        buttonRecordSale = findViewById(R.id.buttonRecordSale);
        buttonSalesHistory = findViewById(R.id.buttonSalesHistory);

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(null, product -> {
            // Navigate to EditProductActivity when a product is clicked
            Intent intent = new Intent(ProductListActivity.this, EditProductActivity.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });
        recyclerViewProducts.setAdapter(productAdapter);

        buttonAddNewProduct.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        buttonRecordSale.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, AddSaleActivity.class);
            startActivity(intent);
        });

        buttonSalesHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, SalesHistoryActivity.class);
            startActivity(intent);
        });

        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    /**
     * Load all products from database and update the RecyclerView.
     */
    private void loadProducts() {
        List<Product> products = dbHelper.getAllProducts();
        
        if (products.isEmpty()) {
            recyclerViewProducts.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerViewProducts.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
            productAdapter.updateProductList(products);
        }
    }
}

