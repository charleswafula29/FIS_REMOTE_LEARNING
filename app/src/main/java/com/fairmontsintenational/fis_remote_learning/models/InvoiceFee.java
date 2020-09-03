package com.fairmontsintenational.fis_remote_learning.models;

public class InvoiceFee {
    private Integer ID, StudentID;
    private String Description,TxnDate;
    private float Debit,Payments,RunningBalance;

    public InvoiceFee(Integer ID, Integer studentID, String description, String txnDate, float debit, float payments, float runningBalance) {
        this.ID = ID;
        StudentID = studentID;
        Description = description;
        TxnDate = txnDate;
        Debit = debit;
        Payments = payments;
        RunningBalance = runningBalance;
    }

    public Integer getID() {
        return ID;
    }

    public Integer getStudentID() {
        return StudentID;
    }

    public String getDescription() {
        return Description;
    }

    public String getTxnDate() {
        return TxnDate;
    }

    public float getDebit() {
        return Debit;
    }

    public float getPayments() {
        return Payments;
    }

    public float getRunningBalance() {
        return RunningBalance;
    }

    @Override
    public String toString() {
        return "InvoiceFee{" +
                "ID=" + ID +
                ", StudentID=" + StudentID +
                ", Description='" + Description + '\'' +
                ", TxnDate='" + TxnDate + '\'' +
                ", Debit=" + Debit +
                ", Payments=" + Payments +
                ", RunningBalance=" + RunningBalance +
                '}';
    }
}
