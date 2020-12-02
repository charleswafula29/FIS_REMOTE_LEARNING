package com.fis.fis_remote_learning.models;

public class RegisteredStudentModel {
    private String code,message,suId;

    public RegisteredStudentModel(String code, String message, String suId) {
        this.code = code;
        this.message = message;
        this.suId = suId;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getSuId() {
        return suId;
    }

    @Override
    public String toString() {
        return "RegisteredStudentModel{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", suId='" + suId + '\'' +
                '}';
    }
}
