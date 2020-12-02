package com.fis.fis_remote_learning.models;

public class RegisterStudentModel {
    private Integer studentId;
    private String firstName,middleName, lastName, gender;
    private Integer level;
    private String dateOfBirth;
    private int parentId;

    public RegisterStudentModel(Integer studentId, String firstName, String middleName, String lastName, String gender, Integer level, String dateOfBirth, int parentId) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.level = level;
        this.dateOfBirth = dateOfBirth;
        this.parentId = parentId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public Integer getLevel() {
        return level;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public int getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return "RegisterStudentModel{" +
                "studentId=" + studentId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", level=" + level +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
