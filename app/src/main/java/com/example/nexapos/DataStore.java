package com.example.nexapos;

import java.util.ArrayList;

public class DataStore {
    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Customer> customers = new ArrayList<>();
    public static ArrayList<SaleRecord> sales = new ArrayList<>();
    public static ArrayList<PurchaseRecord> purchases = new ArrayList<>();

    public static ArrayList<String> getProductNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Product p : products) {
            names.add(p.getName());
        }
        return names;
    }

    public static Product findProduct(String name) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public static double getTotalSales() {
        double sum = 0;
        for (SaleRecord s : sales) {
            sum += s.getTotal();
        }
        return sum;
    }

    public static double getTotalPurchases() {
        double sum = 0;
        for (PurchaseRecord p : purchases) {
            sum += p.getTotal();
        }
        return sum;
    }

    public static double getStockValue() {
        double sum = 0;
        for (Product p : products) {
            sum += p.getQuantity() * p.getPurchasePrice();
        }
        return sum;
    }
}