package com.example.jucygo.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main Activity - Entry point of the application.
 * Navigates to ProductListActivity to display all products.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Navigate directly to ProductListActivity
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
        finish();
    }
}

