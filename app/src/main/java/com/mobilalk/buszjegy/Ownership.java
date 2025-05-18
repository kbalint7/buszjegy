package com.mobilalk.buszjegy;

public class Ownership {
    private String email;
    private String itemId;

    public Ownership(String email, String itemId) {
        this.email = email;
        this.itemId = itemId;
    }

    public String getEmail() {
        return email;
    }

    public String getItemId() {
        return itemId;
    }
}
