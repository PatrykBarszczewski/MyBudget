package com.example.mybudget;

public class Amount {

    private int id;
    private String name;
    private String date;
    private String selectedCategory;
    private String selectedPeriod;
    private String amount;

    public Amount(int id, String name, String date, String selectedCategory, String selectedPeriod, String amount) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.selectedCategory = selectedCategory;
        this.selectedPeriod = selectedPeriod;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Amount{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", selectedCategory='" + selectedCategory + '\'' +
                ", period='" + selectedPeriod + '\'' +
                ", amount=" + amount +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return selectedCategory;
    }

    public void setCategory(String category) {
        this.selectedCategory = category;
    }

    public String getPeriod() {
        return selectedPeriod;
    }

    public void setPeriod(String period) {
        this.selectedPeriod = period;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
