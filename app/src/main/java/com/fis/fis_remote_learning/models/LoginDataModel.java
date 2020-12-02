package com.fis.fis_remote_learning.models;

import java.util.List;

public class LoginDataModel {
    private String parentID;
    private String emailAddress,firstName,gender,accountStatus;
    private List<StudentsRespModel> students;

    public LoginDataModel(String parentID, String emailAddress, String firstName, String gender, String accountStatus, List<StudentsRespModel> students) {
        this.parentID = parentID;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.gender = gender;
        this.accountStatus = accountStatus;
        this.students = students;
    }

    public String getParentID() {
        return parentID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getGender() {
        return gender;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public List<StudentsRespModel> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return "LoginDataModel{" +
                "parentID='" + parentID + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", firstName='" + firstName + '\'' +
                ", gender='" + gender + '\'' +
                ", accountStatus='" + accountStatus + '\'' +
                ", students=" + students +
                '}';
    }
}
