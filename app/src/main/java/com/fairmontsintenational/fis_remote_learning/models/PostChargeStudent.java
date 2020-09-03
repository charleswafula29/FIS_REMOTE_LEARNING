package com.fairmontsintenational.fis_remote_learning.models;

import java.util.List;

public class PostChargeStudent {
    private String SId;
    private List<ChargeStudentModel> StudentCharges;

    public PostChargeStudent(String suId, List<ChargeStudentModel> studentCharges) {
        SId = suId;
        StudentCharges = studentCharges;
    }

    public String getSId() {
        return SId;
    }

    public List<ChargeStudentModel> getStudentCharges() {
        return StudentCharges;
    }

    @Override
    public String toString() {
        return "PostChargeStudent{" +
                "SuId='" + SId + '\'' +
                ", StudentCharges=" + StudentCharges +
                '}';
    }
}
