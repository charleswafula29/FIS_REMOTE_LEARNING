package com.fis.fis_remote_learning.models;

public class RegStudentRespModel {
    RegisteredStudentModel status;

    public RegStudentRespModel(RegisteredStudentModel status) {
        this.status = status;
    }

    public RegisteredStudentModel getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "RegStudentRespModel{" +
                "status=" + status +
                '}';
    }
}
