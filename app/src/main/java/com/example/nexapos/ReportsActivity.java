package com.example.nexapos;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ReportsActivity extends AppCompatActivity {

    private final ArrayList<String> lines = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Spinner spReport = findViewById(R.id.spReport);
        ListView listReport = findViewById(R.id.listReport);
        tvTotal = findViewById(R.id.tvReportTotal);

        adapter = new ArrayAdapter<>(this, R.layout.row_text, R.id.tvRowText, lines);
        listReport.setAdapter(adapter);

        // Refill the list whenever the Spinner selection changes
        spReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadReport(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void loadReport(int position) {
        lines.clear();

        if (position == 0) {
            for (SaleRecord s : DataStore.sales) {
                lines.add(s.toString());
            }
            tvTotal.setText(getString(R.string.total_sales, DataStore.getTotalSales()));
        } else if (position == 1) {
            for (PurchaseRecord p : DataStore.purchases) {
                lines.add(p.toString());
            }
            tvTotal.setText(getString(R.string.total_purchases, DataStore.getTotalPurchases()));
        } else {
            for (Product p : DataStore.products) {
                lines.add(getString(R.string.stock_line, p.getName(), p.getQuantity(),
                        p.getQuantity() * p.getPurchasePrice()));
            }
            tvTotal.setText(getString(R.string.total_stock, DataStore.getStockValue()));
        }

        adapter.notifyDataSetChanged();
    }
}