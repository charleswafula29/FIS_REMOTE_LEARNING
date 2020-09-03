package com.fairmontsintenational.fis_remote_learning.models;

import java.util.List;

public class ChargesRespModel {
    private List<ChargeModel> Charges;

    public ChargesRespModel(List<ChargeModel> charges) {
        Charges = charges;
    }

    public List<ChargeModel> getCharges() {
        return Charges;
    }

    @Override
    public String toString() {
        return "ChargesRespModel{" +
                "Charges=" + Charges +
                '}';
    }
}
