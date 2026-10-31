package com.example.nexapos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.row_product, parent, false);
        }

        Product p = getItem(position);

        TextView tvName = convertView.findViewById(R.id.tvRowName);
        TextView tvCategory = convertView.findViewById(R.id.tvRowCategory);
        TextView tvPrice = convertView.findViewById(R.id.tvRowPrice);
        TextView tvQty = convertView.findViewById(R.id.tvRowQty);

        tvName.setText(p.getName());
        tvCategory.setText(p.getCategory());
        tvPrice.setText(getContext().getString(R.string.money_format, p.getSalePrice()));
        tvQty.setText(getContext().getString(R.string.row_qty, p.getQuantity()));

        if (p.getQuantity() < 5) {
            tvQty.setTextColor(ContextCompat.getColor(getContext(), R.color.nexa_danger));
        } else {
            tvQty.setTextColor(ContextCompat.getColor(getContext(), R.color.nexa_muted));
        }
        return convertView;
    }
}