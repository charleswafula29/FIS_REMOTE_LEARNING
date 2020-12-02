package com.fis.fis_remote_learning.models;

public class ChargeModel {
    private Integer PackageId;
    private String PackageName,Description;
    private Integer Charges;
    private String PackageType;

    public ChargeModel(Integer packageId, String packageName, String description, Integer charges, String packageType) {
        PackageId = packageId;
        PackageName = packageName;
        Description = description;
        Charges = charges;
        PackageType = packageType;
    }

    public Integer getPackageId() {
        return PackageId;
    }

    public String getPackageName() {
        return PackageName;
    }

    public String getDescription() {
        return Description;
    }

    public Integer getCharges() {
        return Charges;
    }

    public String getPackageType() {
        return PackageType;
    }

    @Override
    public String toString() {
        return "ChargeModel{" +
                "PackageId=" + PackageId +
                ", PackageName='" + PackageName + '\'' +
                ", Description='" + Description + '\'' +
                ", Charges=" + Charges +
                ", PackageType='" + PackageType + '\'' +
                '}';
    }
}
