package com.fairmontsintenational.fis_remote_learning.models;

public class StudentModel {
    private Integer id;
    private String ImageUri, StudentNames, level, email, Emailpassword,status,Type;

    public StudentModel(Integer id,String imageUri, String studentNames, String level, String email, String emailpassword, String status,String Type) {
        this.id = id;
        ImageUri = imageUri;
        StudentNames = studentNames;
        this.level = level;
        this.email = email;
        Emailpassword = emailpassword;
        this.status = status;
        this.Type = Type;
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return Type;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public String getStudentNames() {
        return StudentNames;
    }

    public String getLevel() {
        return level;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailpassword() {
        return Emailpassword;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "ImageUri='" + ImageUri + '\'' +
                ", StudentNames='" + StudentNames + '\'' +
                ", level='" + level + '\'' +
                ", email='" + email + '\'' +
                ", Emailpassword='" + Emailpassword + '\'' +
                ", status='" + status + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
