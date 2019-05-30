package com.revoluttask.model;

public class CurrencyModel {
    @Override
    public String toString() {
        return "CurrencyModel{" + "name='" + name + '\'' + ", amount=" + amount + '}';
    }

    String name;
    double amount;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
