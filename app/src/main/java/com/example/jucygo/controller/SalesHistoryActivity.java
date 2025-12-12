package com.example.jucygo.controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jucygo.R;
import com.example.jucygo.model.DBHelper;
import com.example.jucygo.model.Sale;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Activity to display the sales history with search/filter functionality.
 */
public class SalesHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSales;
    private TextView textViewEmpty;
    private SearchView searchViewSales;
    private SalesHistoryAdapter salesAdapter;
    private DBHelper dbHelper;
    private List<Sale> allSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        dbHelper = new DBHelper(this);

        initializeViews();
        setupRecyclerView();
        setupSearchView();
        loadSales();
    }

    /**
     * Initialize all view components.
     */
    private void initializeViews() {
        recyclerViewSales = findViewById(R.id.recyclerViewSales);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        searchViewSales = findViewById(R.id.searchViewSales);
    }

    /**
     * Setup the RecyclerView with adapter and layout manager.
     */
    private void setupRecyclerView() {
        recyclerViewSales.setLayoutManager(new LinearLayoutManager(this));
        salesAdapter = new SalesHistoryAdapter(null);
        recyclerViewSales.setAdapter(salesAdapter);
    }

    /**
     * Setup the SearchView for filtering sales.
     */
    private void setupSearchView() {
        searchViewSales.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterSales(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    loadSales();
                } else {
                    filterSales(newText);
                }
                return false;
            }
        });
    }

    /**
     * Setup button click listeners.
     */
    private void setupButtons() {
        // Back button removed - using system back navigation
    }

    /**
     * Load all sales from database.
     */
    private void loadSales() {
        allSales = dbHelper.getAllSales();
        displaySales(allSales);
    }

    /**
     * Filter sales based on search query (by product name or date).
     */
    private void filterSales(String query) {
        if (TextUtils.isEmpty(query)) {
            displaySales(allSales);
            return;
        }

        List<Sale> filteredSales;
        
        // Try to match as date first (format: YYYY-MM-DD)
        if (query.matches("\\d{4}-\\d{2}-\\d{2}")) {
            filteredSales = dbHelper.searchSalesByDate(query);
        } else {
            // Otherwise search by product name
            filteredSales = dbHelper.searchSalesByProductName(query);
        }

        displaySales(filteredSales);
    }

    /**
     * Display sales in RecyclerView or show empty message.
     */
    private void displaySales(List<Sale> sales) {
        if (sales == null || sales.isEmpty()) {
            recyclerViewSales.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerViewSales.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
            salesAdapter.updateSaleList(sales);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh sales when returning to this activity
        loadSales();
    }
}

