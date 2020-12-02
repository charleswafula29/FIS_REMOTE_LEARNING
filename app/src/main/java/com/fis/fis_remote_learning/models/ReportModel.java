package com.fis.fis_remote_learning.models;

public class ReportModel {

    private Integer DiaryId,StudentId;
    private String Admno;
    private Integer ClassID;
    private String CName,Name,SEX,SysDate,Date,DayEntry,TeacherComment,ParenyComment,Filter,UserName;
    private Boolean IsCancelled,verified;
    private String Teacher;
    private Boolean Uploaded;

    public ReportModel(Integer diaryId, Integer studentId, String admno, Integer classID, String CName, String name, String SEX, String sysDate, String date, String dayEntry,
                       String teacherComment, String parenyComment, String filter, String userName, Boolean isCancelled, Boolean verified, String teacher, Boolean uploaded) {
        DiaryId = diaryId;
        StudentId = studentId;
        Admno = admno;
        ClassID = classID;
        this.CName = CName;
        Name = name;
        this.SEX = SEX;
        SysDate = sysDate;
        Date = date;
        DayEntry = dayEntry;
        TeacherComment = teacherComment;
        ParenyComment = parenyComment;
        Filter = filter;
        UserName = userName;
        IsCancelled = isCancelled;
        this.verified = verified;
        Teacher = teacher;
        Uploaded = uploaded;
    }

    public Integer getDiaryId() {
        return DiaryId;
    }

    public Integer getStudentId() {
        return StudentId;
    }

    public String getAdmno() {
        return Admno;
    }

    public Integer getClassID() {
        return ClassID;
    }

    public String getCName() {
        return CName;
    }

    public String getName() {
        return Name;
    }

    public String getSEX() {
        return SEX;
    }

    public String getSysDate() {
        return SysDate;
    }

    public String getDate() {
        return Date;
    }

    public String getDayEntry() {
        return DayEntry;
    }

    public String getTeacherComment() {
        return TeacherComment;
    }

    public String getParenyComment() {
        return ParenyComment;
    }

    public String getFilter() {
        return Filter;
    }

    public String getUserName() {
        return UserName;
    }

    public Boolean getCancelled() {
        return IsCancelled;
    }

    public Boolean getVerified() {
        return verified;
    }

    public String getTeacher() {
        return Teacher;
    }

    public Boolean getUploaded() {
        return Uploaded;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "DiaryId=" + DiaryId +
                ", StudentId=" + StudentId +
                ", Admno='" + Admno + '\'' +
                ", ClassID=" + ClassID +
                ", CName='" + CName + '\'' +
                ", Name='" + Name + '\'' +
                ", SEX='" + SEX + '\'' +
                ", SysDate='" + SysDate + '\'' +
                ", Date='" + Date + '\'' +
                ", DayEntry='" + DayEntry + '\'' +
                ", TeacherComment='" + TeacherComment + '\'' +
                ", ParenyComment='" + ParenyComment + '\'' +
                ", Filter='" + Filter + '\'' +
                ", UserName='" + UserName + '\'' +
                ", IsCancelled=" + IsCancelled +
                ", verified=" + verified +
                ", Teacher='" + Teacher + '\'' +
                ", Uploaded=" + Uploaded +
                '}';
    }
}
