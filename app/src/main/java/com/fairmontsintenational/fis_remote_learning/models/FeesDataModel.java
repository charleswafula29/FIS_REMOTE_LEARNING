package com.fairmontsintenational.fis_remote_learning.models;

import java.util.List;

public class FeesDataModel {
    private List<InvoiceFee> Report;

    public FeesDataModel(List<InvoiceFee> report) {
        Report = report;
    }

    public List<InvoiceFee> getReport() {
        return Report;
    }

    @Override
    public String toString() {
        return "FeesDataModel{" +
                "Report=" + Report +
                '}';
    }
}
