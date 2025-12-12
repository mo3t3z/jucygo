package com.example.jucygo.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jucygo.R;
import com.example.jucygo.model.DBHelper;
import com.example.jucygo.model.Order;

import java.util.List;

/**
 * Activity to display the history of all orders.
 * Supports search by customer name, product name, or date.
 */
public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private TextView textViewEmpty;
    private SearchView searchViewOrders;
    private OrderHistoryAdapter orderAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        dbHelper = new DBHelper(this);

        initializeViews();
        setupRecyclerView();
        setupSearchView();
        loadOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    /**
     * Initialize all view components.
     */
    private void initializeViews() {
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        searchViewOrders = findViewById(R.id.searchViewOrders);
    }

    /**
     * Setup the RecyclerView with adapter.
     */
    private void setupRecyclerView() {
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderHistoryAdapter(null, dbHelper, this::loadOrders);
        recyclerViewOrders.setAdapter(orderAdapter);
    }

    /**
     * Setup the search functionality.
     */
    private void setupSearchView() {
        searchViewOrders.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchOrders(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadOrders();
                } else {
                    searchOrders(newText);
                }
                return true;
            }
        });
    }

    /**
     * Load all orders from database.
     */
    private void loadOrders() {
        List<Order> orders = dbHelper.getAllOrders();
        updateUI(orders);
    }

    /**
     * Search orders based on query (customer name, product name, or date).
     */
    private void searchOrders(String query) {
        List<Order> orders;
        
        // Check if query looks like a date (starts with digits)
        if (query.matches("^\\d{4}-.*")) {
            orders = dbHelper.searchOrdersByDate(query);
        } else {
            // Search by customer name first
            orders = dbHelper.searchOrdersByCustomerName(query);
            
            // If no results, try searching by product name
            if (orders.isEmpty()) {
                orders = dbHelper.searchOrdersByProductName(query);
            }
        }
        
        updateUI(orders);
    }

    /**
     * Update the UI based on the order list.
     */
    private void updateUI(List<Order> orders) {
        if (orders.isEmpty()) {
            recyclerViewOrders.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerViewOrders.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
            orderAdapter.updateOrderList(orders);
        }
    }
}
