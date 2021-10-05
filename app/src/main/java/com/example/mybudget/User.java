package com.example.mybudget;

public class User {

    private String userName;
    private int monthlyIncome;
    private int plannedSavings;

    public User(String userName, int monthlyIncome, int plannedSavings){
        this.userName = userName;
        this.monthlyIncome = monthlyIncome;
        this.plannedSavings = plannedSavings;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public int getMonthlyIncome() { return monthlyIncome; }

    public void setMonthlyIncome(int monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public int getPlannedSavings() { return plannedSavings; }

    public void setPlannedSavings(int plannedSavings) { this.plannedSavings = plannedSavings; }
}
