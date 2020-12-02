package com.fis.fis_remote_learning.models;

public class LevelsModel {

    private int ClassID;
    private String ClassName,CName;
    private int PeriodId;
    private String ClasPeriod;
    private boolean Active;
    private String Description;
    private boolean PeriodClosed, IsClosed;
    private int PeriodActive, NoOfStudents, BranchID;

    public LevelsModel(int classID, String className, String CName, int periodId, String clasPeriod, boolean active,
                       String description, boolean periodClosed, boolean isClosed, int periodActive, int noOfStudents, int branchID) {
        ClassID = classID;
        ClassName = className;
        this.CName = CName;
        PeriodId = periodId;
        ClasPeriod = clasPeriod;
        Active = active;
        Description = description;
        PeriodClosed = periodClosed;
        IsClosed = isClosed;
        PeriodActive = periodActive;
        NoOfStudents = noOfStudents;
        BranchID = branchID;
    }

    public int getClassID() {
        return ClassID;
    }

    public String getClassName() {
        return ClassName;
    }

    public String getCName() {
        return CName;
    }

    public int getPeriodId() {
        return PeriodId;
    }

    public String getClasPeriod() {
        return ClasPeriod;
    }

    public boolean isActive() {
        return Active;
    }

    public String getDescription() {
        return Description;
    }

    public boolean isPeriodClosed() {
        return PeriodClosed;
    }

    public boolean isClosed() {
        return IsClosed;
    }

    public int getPeriodActive() {
        return PeriodActive;
    }

    public int getNoOfStudents() {
        return NoOfStudents;
    }

    public int getBranchID() {
        return BranchID;
    }

    @Override
    public String toString() {
        return "LevelsModel{" +
                "ClassID=" + ClassID +
                ", ClassName='" + ClassName + '\'' +
                ", CName='" + CName + '\'' +
                ", PeriodId=" + PeriodId +
                ", ClasPeriod='" + ClasPeriod + '\'' +
                ", Active=" + Active +
                ", Description='" + Description + '\'' +
                ", PeriodClosed=" + PeriodClosed +
                ", IsClosed=" + IsClosed +
                ", PeriodActive=" + PeriodActive +
                ", NoOfStudents=" + NoOfStudents +
                ", BranchID=" + BranchID +
                '}';
    }
}
