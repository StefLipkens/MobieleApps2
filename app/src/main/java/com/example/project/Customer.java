package com.example.project;

public class Customer {
    private String id;
    private String name;
    private String telNr;
    private String status;
    private String lastTimePaid;
    private String history;

    public Customer(String id, String name, String telNr, String status, String lastTimePaid)
    {
        this.id = id;
        this.name=name;
        this.telNr=telNr;
        this.status=status;
        this.lastTimePaid=lastTimePaid;
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

    public String getTelNr() {
        return telNr;
    }

    public void setTelNr(String telNr) {
        this.telNr = telNr;
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
}
