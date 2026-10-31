package com.example.nexapos;

import java.util.Locale;

public class SaleRecord {
    private String productName;
    private String customerName;
    private int quantity;
    private double price;
    private int discount;
    private double total;
    private String paymentMethod;
    private String dateTime;

    public SaleRecord(String productName, String customerName, int quantity, double price,
                      int discount, double total, String paymentMethod, String dateTime) {
        this.productName = productName;
        this.customerName = customerName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.dateTime = dateTime;
    }

    public String getProductName() { return productName; }
    public String getCustomerName() { return customerName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public int getDiscount() { return discount; }
    public double getTotal() { return total; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getDateTime() { return dateTime; }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "%s\n%s  •  %s x %d  •  %d%% off\nRs. %.2f  (%s)",
                dateTime, customerName, productName, quantity, discount, total, paymentMethod);
    }
}