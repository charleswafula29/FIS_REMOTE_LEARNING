package com.fairmontsintenational.fis_remote_learning.models;

public class CancelSubscriptionModel {
    private Integer StudentID;
    private String Comment;

    public CancelSubscriptionModel(Integer studentID, String comment) {
        StudentID = studentID;
        Comment = comment;
    }

    @Override
    public String toString() {
        return "CancelSubscriptionModel{" +
                "StudentID=" + StudentID +
                ", Comment='" + Comment + '\'' +
                '}';
    }
}
