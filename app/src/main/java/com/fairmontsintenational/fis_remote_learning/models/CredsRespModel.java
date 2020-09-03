package com.fairmontsintenational.fis_remote_learning.models;

import java.util.List;

public class CredsRespModel {
    private List<CredentialsModel> Credentials;

    public CredsRespModel(List<CredentialsModel> credentials) {
        Credentials = credentials;
    }

    public List<CredentialsModel> getCredentials() {
        return Credentials;
    }

    @Override
    public String toString() {
        return "CredsRespModel{" +
                "Credentials=" + Credentials +
                '}';
    }
}
