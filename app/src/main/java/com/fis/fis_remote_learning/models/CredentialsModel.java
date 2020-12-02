package com.fis.fis_remote_learning.models;

public class CredentialsModel {
    private Integer Id,StudentId;
    private String PlatForm,UserName,Password;

    public CredentialsModel(Integer id, Integer studentId, String platForm, String userName, String password) {
        Id = id;
        StudentId = studentId;
        PlatForm = platForm;
        UserName = userName;
        Password = password;
    }

    public Integer getId() {
        return Id;
    }

    public Integer getStudentId() {
        return StudentId;
    }

    public String getPlatForm() {
        return PlatForm;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    @Override
    public String toString() {
        return "CredentialsModel{" +
                "Id=" + Id +
                ", StudentId=" + StudentId +
                ", PlatForm='" + PlatForm + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
