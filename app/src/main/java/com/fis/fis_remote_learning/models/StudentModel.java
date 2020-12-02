package com.fis.fis_remote_learning.models;

public class StudentModel {
    private Integer Sid;
    private String StudentNames,Gender,className,AdmNo,Status,Type;
    private Integer Balances;
    private String SUID;

    public StudentModel(Integer sid, String studentNames, String gender, String className, String admNo, String status, String type, Integer balances, String SUID) {
        Sid = sid;
        StudentNames = studentNames;
        Gender = gender;
        this.className = className;
        AdmNo = admNo;
        Status = status;
        Type = type;
        Balances = balances;
        this.SUID = SUID;
    }

    public Integer getSid() {
        return Sid;
    }

    public String getStudentNames() {
        return StudentNames;
    }

    public String getGender() {
        return Gender;
    }

    public String getClassName() {
        return className;
    }

    public String getAdmNo() {
        return AdmNo;
    }

    public String getStatus() {
        return Status;
    }

    public String getType() {
        return Type;
    }

    public Integer getBalances() {
        return Balances;
    }

    public String getSUID() {
        return SUID;
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "Sid=" + Sid +
                ", StudentNames='" + StudentNames + '\'' +
                ", Gender='" + Gender + '\'' +
                ", className='" + className + '\'' +
                ", AdmNo='" + AdmNo + '\'' +
                ", Status='" + Status + '\'' +
                ", Type='" + Type + '\'' +
                ", Balances=" + Balances +
                ", SUID='" + SUID + '\'' +
                '}';
    }
}
