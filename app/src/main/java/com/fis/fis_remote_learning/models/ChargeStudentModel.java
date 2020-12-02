package com.fis.fis_remote_learning.models;

public class ChargeStudentModel {
    private Integer PackageId;
    private String PackageType,PackageName;
    private Integer Quantity,Charges;

    public ChargeStudentModel(Integer packageId, String packageType, String packageName, Integer quantity, Integer charges) {
        PackageId = packageId;
        PackageType = packageType;
        PackageName = packageName;
        Quantity = quantity;
        Charges = charges;
    }

    public Integer getPackageId() {
        return PackageId;
    }

    public String getPackageType() {
        return PackageType;
    }

    public String getPackageName() {
        return PackageName;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public Integer getCharges() {
        return Charges;
    }

    @Override
    public String toString() {
        return "ChargeStudentModel{" +
                "PackageId=" + PackageId +
                ", PackageType='" + PackageType + '\'' +
                ", PackageName='" + PackageName + '\'' +
                ", Quantity=" + Quantity +
                ", Charges=" + Charges +
                '}';
    }
}
