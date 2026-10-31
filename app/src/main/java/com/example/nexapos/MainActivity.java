package com.example.nexapos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnProducts = findViewById(R.id.btnProducts);
        Button btnCustomers = findViewById(R.id.btnCustomers);
        Button btnSale = findViewById(R.id.btnSale);
        Button btnPurchase = findViewById(R.id.btnPurchase);
        Button btnReports = findViewById(R.id.btnReports);

        btnProducts.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProductsActivity.class)));
        btnCustomers.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CustomersActivity.class)));
        btnSale.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SaleActivity.class)));
        btnPurchase.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PurchaseActivity.class)));
        btnReports.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ReportsActivity.class)));
    }
}