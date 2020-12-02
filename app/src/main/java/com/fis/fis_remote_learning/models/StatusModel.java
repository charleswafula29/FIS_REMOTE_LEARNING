package com.fis.fis_remote_learning.models;

public class StatusModel {
    private String code;
    private String message;

    public StatusModel(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "StatusModel{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
