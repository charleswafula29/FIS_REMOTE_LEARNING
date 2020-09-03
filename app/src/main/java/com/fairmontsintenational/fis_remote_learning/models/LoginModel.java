package com.fairmontsintenational.fis_remote_learning.models;

public class LoginModel {
    private StatusModel status;
    private LoginDataModel Data;

    public LoginModel(StatusModel status, LoginDataModel data) {
        this.status = status;
        Data = data;
    }

    public StatusModel getStatus() {
        return status;
    }

    public LoginDataModel getData() {
        return Data;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "status=" + status +
                ", Data=" + Data +
                '}';
    }
}
