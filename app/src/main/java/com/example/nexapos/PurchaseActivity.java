package com.example.nexapos;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PurchaseActivity extends AppCompatActivity {

    private Spinner spProduct;
    private EditText etPrice, etQty;
    private ToggleButton tbStatus;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        spProduct = findViewById(R.id.spProduct);
        etPrice = findViewById(R.id.etPrice);
        etQty = findViewById(R.id.etQty);
        tbStatus = findViewById(R.id.tbStatus);
        tvTotal = findViewById(R.id.tvTotal);
        Button btnComplete = findViewById(R.id.btnCompletePurchase);

        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, DataStore.getProductNames());
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProduct.setAdapter(productAdapter);

        // Pre-fill the current purchase price of the selected product
        spProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Product p = DataStore.findProduct(parent.getItemAtPosition(position).toString());
                if (p != null) {
                    etPrice.setText(String.valueOf(p.getPurchasePrice()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnComplete.setOnClickListener(v -> completePurchase());
    }

    private void completePurchase() {
        if (spProduct.getSelectedItem() == null) {
            Toast.makeText(this, R.string.msg_select_product, Toast.LENGTH_SHORT).show();
            return;
        }

        String qtyText = etQty.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        if (qtyText.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, R.string.msg_fill_all, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            double price = Double.parseDouble(priceText);
            if (qty <= 0) {
                Toast.makeText(this, R.string.msg_enter_quantity, Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = DataStore.findProduct(spProduct.getSelectedItem().toString());
            if (product == null) {
                Toast.makeText(this, R.string.msg_select_product, Toast.LENGTH_SHORT).show();
                return;
            }

            double total = qty * price;
            String status = tbStatus.isChecked()
                    ? getString(R.string.status_paid)
                    : getString(R.string.status_unpaid);
            String dateTime = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    .format(new Date());

            product.setQuantity(product.getQuantity() + qty);
            product.setPurchasePrice(price);
            DataStore.purchases.add(new PurchaseRecord(product.getName(), qty, price,
                    total, status, dateTime));

            tvTotal.setText(getString(R.string.money_format, total));
            etQty.setText("");
            Toast.makeText(this, R.string.msg_purchase_done, Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.msg_fill_all, Toast.LENGTH_SHORT).show();
        }
    }
}