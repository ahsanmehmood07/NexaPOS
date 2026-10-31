package com.example.nexapos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductsActivity extends AppCompatActivity {

    private EditText etName, etSalePrice, etPurchasePrice, etQuantity;
    private Spinner spCategory;
    private CheckBox cbTaxable;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ListView listProducts = findViewById(R.id.listProducts);

        // The form is placed at the top of the list as a header view
        View form = LayoutInflater.from(this).inflate(R.layout.form_product, listProducts, false);
        listProducts.addHeaderView(form, null, false);

        etName = form.findViewById(R.id.etProductName);
        etSalePrice = form.findViewById(R.id.etSalePrice);
        etPurchasePrice = form.findViewById(R.id.etPurchasePrice);
        etQuantity = form.findViewById(R.id.etQuantity);
        spCategory = form.findViewById(R.id.spCategory);
        cbTaxable = form.findViewById(R.id.cbTaxable);
        Button btnAdd = form.findViewById(R.id.btnAddProduct);
        Button btnClear = form.findViewById(R.id.btnClearProduct);

        adapter = new ProductAdapter(this, DataStore.products);
        listProducts.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> addProduct());
        btnClear.setOnClickListener(v -> clearFields());

        // Long press removes the product. The header counts as position 0.
        listProducts.setOnItemLongClickListener((parent, view, position, id) -> {
            int index = position - listProducts.getHeaderViewsCount();
            if (index >= 0 && index < DataStore.products.size()) {
                Product removed = DataStore.products.remove(index);
                adapter.notifyDataSetChanged();
                Toast.makeText(this,
                        getString(R.string.msg_product_deleted, removed.getName()),
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void addProduct() {
        String name = etName.getText().toString().trim();
        String salePriceText = etSalePrice.getText().toString().trim();
        String purchasePriceText = etPurchasePrice.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();

        if (name.isEmpty() || salePriceText.isEmpty()
                || purchasePriceText.isEmpty() || quantityText.isEmpty()) {
            Toast.makeText(this, R.string.msg_fill_all, Toast.LENGTH_SHORT).show();
            return;
        }

        if (DataStore.findProduct(name) != null) {
            Toast.makeText(this, R.string.msg_product_exists, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double salePrice = Double.parseDouble(salePriceText);
            double purchasePrice = Double.parseDouble(purchasePriceText);
            int quantity = Integer.parseInt(quantityText);
            String category = spCategory.getSelectedItem().toString();

            Product product = new Product(name, category, salePrice,
                    purchasePrice, quantity, cbTaxable.isChecked());
            DataStore.products.add(product);

            adapter.notifyDataSetChanged();
            clearFields();
            Toast.makeText(this, R.string.msg_product_added, Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.msg_fill_all, Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etName.setText("");
        etSalePrice.setText("");
        etPurchasePrice.setText("");
        etQuantity.setText("");
        cbTaxable.setChecked(false);
        spCategory.setSelection(0);
        etName.requestFocus();
    }
}