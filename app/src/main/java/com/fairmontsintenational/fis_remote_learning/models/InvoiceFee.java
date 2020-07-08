package com.fairmontsintenational.fis_remote_learning.models;

public class InvoiceFee {
    private String date;
    private String amount;

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getAmount_debit() {
        return amount_debit;
    }

    public String getPeriod() {
        return period;
    }

    public String getDescription() {
        return description;
    }

    private String amount_debit;
    private String period;
    private String description;

    public InvoiceFee(String date, String amount, String amount_debit, String period, String description) {
        this.date = date;
        this.amount = amount;
        this.amount_debit = amount_debit;
        this.period = period;
        this.description = description;
    }
}
