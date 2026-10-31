package com.example.nexapos;

public class Product {
    private String name;
    private String category;
    private double salePrice;
    private double purchasePrice;
    private int quantity;
    private boolean taxable;

    public Product(String name, String category, double salePrice,
                   double purchasePrice, int quantity, boolean taxable) {
        this.name = name;
        this.category = category;
        this.salePrice = salePrice;
        this.purchasePrice = purchasePrice;
        this.quantity = quantity;
        this.taxable = taxable;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getSalePrice() { return salePrice; }
    public double getPurchasePrice() { return purchasePrice; }
    public int getQuantity() { return quantity; }
    public boolean isTaxable() { return taxable; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }
}