package com.fairmontsintenational.fis_remote_learning.models;

import android.content.Intent;

public class RegistrationModel {
    private Integer ParentId;
    private String firstName,lastName,PhoneNumber,Email,gender,password,spouseFirstName, spouseLastName,spousePhoneNumber,
            spouseGender, spousePassword,spouseEmail;
    private Boolean Active;

    public RegistrationModel(Integer parentId, String firstName, String lastName, String phoneNumber,
                             String email, String gender, String password, String spouseFirstName, String spouseLastName, String spousePhoneNumber,
                             String spouseGender, String spousePassword, String spouseEmail, Boolean active) {
        ParentId = parentId;
        this.firstName = firstName;
        this.lastName = lastName;
        PhoneNumber = phoneNumber;
        Email = email;
        this.gender = gender;
        this.password = password;
        this.spouseFirstName = spouseFirstName;
        this.spouseLastName = spouseLastName;
        this.spousePhoneNumber = spousePhoneNumber;
        this.spouseGender = spouseGender;
        this.spousePassword = spousePassword;
        this.spouseEmail = spouseEmail;
        Active = active;
    }

    public Integer getParentId() {
        return ParentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getSpouseFirstName() {
        return spouseFirstName;
    }

    public String getSpouseLastName() {
        return spouseLastName;
    }

    public String getSpousePhoneNumber() {
        return spousePhoneNumber;
    }

    public String getSpouseGender() {
        return spouseGender;
    }

    public String getSpousePassword() {
        return spousePassword;
    }

    public String getSpouseEmail() {
        return spouseEmail;
    }

    public Boolean isActive() {
        return Active;
    }

    @Override
    public String toString() {
        return "RegistrationModel{" +
                "ParentId=" + ParentId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Email='" + Email + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", spouseFirstName='" + spouseFirstName + '\'' +
                ", spouseLastName='" + spouseLastName + '\'' +
                ", spousePhoneNumber='" + spousePhoneNumber + '\'' +
                ", spouseGender='" + spouseGender + '\'' +
                ", spousePassword='" + spousePassword + '\'' +
                ", spouseEmail='" + spouseEmail + '\'' +
                ", Active=" + Active +
                '}';
    }
}
