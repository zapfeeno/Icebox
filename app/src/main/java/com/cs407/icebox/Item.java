package com.cs407.icebox;

public class Item {

    private String purchaseDate;
    private String expDate;
    private String itemName;
    private int id;


    public Item(String itemName, String purchaseDate, String expDate) {

        this.itemName = itemName;
        this.purchaseDate = purchaseDate;
        this.expDate = expDate;

    }

    public String getItemName() {
        return itemName;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public int getId() { return id; }
}
