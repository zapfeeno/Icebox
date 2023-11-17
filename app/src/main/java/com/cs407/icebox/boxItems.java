package com.cs407.icebox;

public class boxItems {

    private String purchaseDate;
    private String expDate;
    private String itemName;
    private String id;


    public boxItems(String itemName, String purchaseDate, String expDate, String id) {

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
