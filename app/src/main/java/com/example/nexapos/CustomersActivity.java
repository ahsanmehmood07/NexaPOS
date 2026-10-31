package com.example.nexapos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomersActivity extends AppCompatActivity {

    private EditText etName, etPhone, etAddress;
    private RadioGroup rgType;
    private ArrayAdapter<Customer> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        // true when the Sale screen opened this screen to pick a customer
        boolean selectMode = getIntent().getBooleanExtra("select_mode", false);

        ListView listCustomers = findViewById(R.id.listCustomers);
        View form = LayoutInflater.from(this).inflate(R.layout.form_customer, listCustomers, false);
        listCustomers.addHeaderView(form, null, false);

        etName = form.findViewById(R.id.etCustomerName);
        etPhone = form.findViewById(R.id.etPhone);
        etAddress = form.findViewById(R.id.etAddress);
        rgType = form.findViewById(R.id.rgType);
        Button btnAdd = form.findViewById(R.id.btnAddCustomer);
        Button btnClear = form.findViewById(R.id.btnClearCustomer);

        adapter = new ArrayAdapter<>(this, R.layout.row_text, R.id.tvRowText, DataStore.customers);
        listCustomers.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> addCustomer());
        btnClear.setOnClickListener(v -> clearFields());

        listCustomers.setOnItemClickListener((parent, view, position, id) -> {
            int index = position - listCustomers.getHeaderViewsCount();
            if (index < 0) return;
            Customer c = DataStore.customers.get(index);

            if (selectMode) {
                Intent result = new Intent();
                result.putExtra("customer_name", c.getName());
                setResult(RESULT_OK, result);
                finish();
            } else {
                Toast.makeText(this,
                        getString(R.string.msg_customer_selected, c.getName()),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Long press opens the phone dialer (implicit Intent)
        listCustomers.setOnItemLongClickListener((parent, view, position, id) -> {
            int index = position - listCustomers.getHeaderViewsCount();
            if (index >= 0 && index < DataStore.customers.size()) {
                Customer c = DataStore.customers.get(index);
                Intent dial = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + Uri.encode(c.getPhone())));
                startActivity(dial);
            }
            return true;
        });
    }

    private void addCustomer() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, R.string.msg_fill_all, Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selected = rgType.findViewById(rgType.getCheckedRadioButtonId());
        String type = selected.getText().toString();

        DataStore.customers.add(new Customer(name, phone, address, type));
        adapter.notifyDataSetChanged();
        clearFields();
        Toast.makeText(this, R.string.msg_customer_added, Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etAddress.setText("");
        rgType.check(R.id.rbRetail);
        etName.requestFocus();
    }
}