package com.example.nexapos;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SaleActivity extends AppCompatActivity {

    private static final int REQUEST_CUSTOMER = 101;

    private Spinner spProduct;
    private TextView tvPrice, tvStock, tvDiscount, tvCustomer, tvTotal;
    private EditText etQty;
    private RadioGroup rgPayment;
    private SeekBar seekDiscount;
    private String selectedCustomer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        spProduct = findViewById(R.id.spProduct);
        tvPrice = findViewById(R.id.tvPrice);
        tvStock = findViewById(R.id.tvStock);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvCustomer = findViewById(R.id.tvCustomer);
        tvTotal = findViewById(R.id.tvTotal);
        etQty = findViewById(R.id.etQty);
        rgPayment = findViewById(R.id.rgPayment);
        seekDiscount = findViewById(R.id.seekDiscount);
        Button btnSelectCustomer = findViewById(R.id.btnSelectCustomer);
        Button btnComplete = findViewById(R.id.btnCompleteSale);

        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, DataStore.getProductNames());
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProduct.setAdapter(productAdapter);

        // When a product is selected, show its price and stock
        spProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Product p = DataStore.findProduct(parent.getItemAtPosition(position).toString());
                if (p != null) {
                    tvPrice.setText(getString(R.string.money_format, p.getSalePrice()));
                    tvStock.setText(getString(R.string.stock_available, p.getQuantity()));
                }
                showTotalPreview();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Discount value text and live total
        seekDiscount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDiscount.setText(getString(R.string.discount_value, progress));
                showTotalPreview();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Live total while typing the quantity
        etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showTotalPreview();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnSelectCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(SaleActivity.this, CustomersActivity.class);
            intent.putExtra("select_mode", true);
            startActivityForResult(intent, REQUEST_CUSTOMER);
        });

        btnComplete.setOnClickListener(v -> completeSale());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CUSTOMER && resultCode == RESULT_OK && data != null) {
            selectedCustomer = data.getStringExtra("customer_name");
            tvCustomer.setText(getString(R.string.customer_selected, selectedCustomer));
        }
    }

    private double calculateTotal(Product product, int qty, int discount) {
        double gross = qty * product.getSalePrice();
        return gross - (gross * discount / 100.0);
    }

    private void showTotalPreview() {
        String qtyText = etQty.getText().toString().trim();
        Product p = null;
        if (spProduct.getSelectedItem() != null) {
            p = DataStore.findProduct(spProduct.getSelectedItem().toString());
        }

        if (p == null || qtyText.isEmpty()) {
            tvTotal.setText(R.string.money_zero);
            return;
        }

        try {
            int qty = Integer.parseInt(qtyText);
            double total = calculateTotal(p, qty, seekDiscount.getProgress());
            tvTotal.setText(getString(R.string.money_format, total));
        } catch (NumberFormatException e) {
            tvTotal.setText(R.string.money_zero);
        }
    }

    private void completeSale() {
        if (spProduct.getSelectedItem() == null) {
            Toast.makeText(this, R.string.msg_select_product, Toast.LENGTH_SHORT).show();
            return;
        }

        String qtyText = etQty.getText().toString().trim();
        if (qtyText.isEmpty()) {
            Toast.makeText(this, R.string.msg_enter_quantity, Toast.LENGTH_SHORT).show();
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.msg_enter_quantity, Toast.LENGTH_SHORT).show();
            return;
        }
        if (qty <= 0) {
            Toast.makeText(this, R.string.msg_enter_quantity, Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = DataStore.findProduct(spProduct.getSelectedItem().toString());
        if (product == null) {
            Toast.makeText(this, R.string.msg_select_product, Toast.LENGTH_SHORT).show();
            return;
        }

        if (qty > product.getQuantity()) {
            Toast.makeText(this, R.string.msg_no_stock, Toast.LENGTH_SHORT).show();
            return;
        }

        int discount = seekDiscount.getProgress();
        double total = calculateTotal(product, qty, discount);

        RadioButton selectedPayment = findViewById(rgPayment.getCheckedRadioButtonId());
        String payment = selectedPayment.getText().toString();

        String customer = selectedCustomer.isEmpty()
                ? getString(R.string.walk_in) : selectedCustomer;
        String dateTime = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(new Date());

        product.setQuantity(product.getQuantity() - qty);
        DataStore.sales.add(new SaleRecord(product.getName(), customer, qty,
                product.getSalePrice(), discount, total, payment, dateTime));

        // Reset the form for the next sale, then show the total that was just saved
        etQty.setText("");
        selectedCustomer = "";
        tvCustomer.setText(R.string.customer_none);
        tvStock.setText(getString(R.string.stock_available, product.getQuantity()));
        tvTotal.setText(getString(R.string.money_format, total));

        Toast.makeText(this, R.string.msg_sale_done, Toast.LENGTH_SHORT).show();
    }
}