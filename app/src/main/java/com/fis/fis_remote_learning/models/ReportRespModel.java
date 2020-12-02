package com.fis.fis_remote_learning.models;

import java.util.List;

public class ReportRespModel {
    private List<ReportModel> Report;

    public ReportRespModel(List<ReportModel> report) {
        Report = report;
    }

    public List<ReportModel> getReport() {
        return Report;
    }

    @Override
    public String toString() {
        return "ReportRespModel{" +
                "Report=" + Report +
                '}';
    }
}
