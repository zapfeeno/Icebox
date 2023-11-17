package com.cs407.icebox;

public class Item {

    private String purchaseDate;
    private String expDate;
    private String itemName;
    private String id;


    public Item(String itemName, String purchaseDate, String expDate, String id) {
        this.itemName = itemName;
        this.purchaseDate = purchaseDate;
        this.expDate = expDate;
        this.id = id;

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

    public String getId() {
        return id;
    }
}