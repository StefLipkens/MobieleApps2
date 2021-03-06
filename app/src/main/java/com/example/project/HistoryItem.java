package com.example.project;

public class HistoryItem{
    private String price;
    private String description;
    private String orderDateTime;

    //wordt gebruikt bij opstarten detailpagina indien customer een history heeft
    public HistoryItem(){}

    public HistoryItem(String price, String description, String orderDateTime) {
        this.price = price;
        this.description = description;
        this.orderDateTime=orderDateTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

}
