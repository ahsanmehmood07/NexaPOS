package com.example.nexapos;

import java.util.Locale;

public class PurchaseRecord {
    private String productName;
    private int quantity;
    private double price;
    private double total;
    private String paymentStatus;
    private String dateTime;

    public PurchaseRecord(String productName, int quantity, double price,
                          double total, String paymentStatus, String dateTime) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.paymentStatus = paymentStatus;
        this.dateTime = dateTime;
    }

    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public double getTotal() { return total; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getDateTime() { return dateTime; }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%s\n%s x %d at Rs. %.2f\nRs. %.2f  (%s)",
                dateTime, productName, quantity, price, total, paymentStatus);
    }
}