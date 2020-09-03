package com.fairmontsintenational.fis_remote_learning.models;

public class UploadStudentPicModel {
    private Integer SId;
    private String ImageinBase64;

    public UploadStudentPicModel(Integer SId, String imageinBase64) {
        this.SId = SId;
        ImageinBase64 = imageinBase64;
    }

    public Integer getSId() {
        return SId;
    }

    public String getImageinBase64() {
        return ImageinBase64;
    }

    @Override
    public String toString() {
        return "UploadStudentPicModel{" +
                "SId=" + SId +
                ", ImageinBase64='" + ImageinBase64 + '\'' +
                '}';
    }
}
