package com.example.project;


import java.util.ArrayList;

public class Customer {
    private String id;
    private String name;
    private String telNr;
    private String status;
    private String lastTimePaid;
    private String lastTimeOrdered;
    private ArrayList<HistoryItem> history;

    public Customer(){}

    public Customer(String id, String name, String telNr, String status, String lastTimePaid,String lastTimeOrdered, ArrayList<HistoryItem> history) {
        this.id = id;
        this.name=name;
        this.telNr=telNr;
        this.status=status;
        this.lastTimePaid=lastTimePaid;
        this.lastTimeOrdered=lastTimeOrdered;
        this.history=history;
    }

    public String getTelNr() {
        return telNr;
    }

    public void setTelNr(String telNr) {
        this.telNr = telNr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastTimePaid() {
        return lastTimePaid;
    }

    public void setLastTimePaid(String lastTimePaid) {
        this.lastTimePaid = lastTimePaid;
    }

    public String getLastTimeOrdered() {
        return lastTimeOrdered;
    }

    public void setLastTimeOrdered(String lastTimeOrdered) {
        this.lastTimeOrdered = lastTimeOrdered;
    }

    public ArrayList<HistoryItem> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<HistoryItem> history) {
        this.history = history;
    }

}
