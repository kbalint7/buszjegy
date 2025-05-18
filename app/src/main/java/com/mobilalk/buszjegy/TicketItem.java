package com.mobilalk.buszjegy;

public class TicketItem {
    private String id;
    private String title;
    private String description;
    private String price;

    public TicketItem() {}

    public TicketItem(String title, String description, String price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String _getId() {
        return id;
    }

    public void _setId(String id) {
        this.id = id;
    }
}
