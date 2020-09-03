package com.fairmontsintenational.fis_remote_learning.models;

public class StudentsRespModel {

    private int SId;
    private String SUID, Name, Admno, SEX;
    private int ClassID;
    private String CName;
    private Integer StreamID;
    private String StreamName, ParentName, DOB, ParentID, ParentCellNo, Parent2Name, Parent2Phone,
            GuardianName, GuardianPhone, GuardianRelationship, GuardianOccupation, Home, HomeAddress, HomeLocation;
    private Boolean Active;
    private String TrxDate;
    private String RegisteredOn, Filter,TransportPackageId,TransportPackage;
    private Integer HasSubscription;
    private String PackageId,PackageName,UserName,SubScriptionNextDate,SubScriptionStartDate,SubScriptionDate;
    private Boolean ActiveSubscription,ParentCancelSubscription;
    private String Description,ID,Grandtotal,Payments,TotalWeivered,Balances,Status;

    public StudentsRespModel(int SId, String SUID, String name, String admno, String SEX, int classID, String CName, Integer streamID, String streamName,
                             String parentName, String DOB, String parentID, String parentCellNo, String parent2Name, String parent2Phone, String guardianName,
                             String guardianPhone, String guardianRelationship, String guardianOccupation, String home, String homeAddress, String homeLocation,
                             Boolean active, String trxDate, String registeredOn, String filter, String transportPackageId, String transportPackage, Integer hasSubscription,
                             String packageId, String packageName, String userName, String subScriptionNextDate, String subScriptionStartDate, String subScriptionDate,
                             Boolean activeSubscription, Boolean parentCancelSubscription, String description, String ID, String grandtotal, String payments, String totalWeivered,
                             String balances, String status) {
        this.SId = SId;
        this.SUID = SUID;
        Name = name;
        Admno = admno;
        this.SEX = SEX;
        ClassID = classID;
        this.CName = CName;
        StreamID = streamID;
        StreamName = streamName;
        ParentName = parentName;
        this.DOB = DOB;
        ParentID = parentID;
        ParentCellNo = parentCellNo;
        Parent2Name = parent2Name;
        Parent2Phone = parent2Phone;
        GuardianName = guardianName;
        GuardianPhone = guardianPhone;
        GuardianRelationship = guardianRelationship;
        GuardianOccupation = guardianOccupation;
        Home = home;
        HomeAddress = homeAddress;
        HomeLocation = homeLocation;
        Active = active;
        TrxDate = trxDate;
        RegisteredOn = registeredOn;
        Filter = filter;
        TransportPackageId = transportPackageId;
        TransportPackage = transportPackage;
        HasSubscription = hasSubscription;
        PackageId = packageId;
        PackageName = packageName;
        UserName = userName;
        SubScriptionNextDate = subScriptionNextDate;
        SubScriptionStartDate = subScriptionStartDate;
        SubScriptionDate = subScriptionDate;
        ActiveSubscription = activeSubscription;
        ParentCancelSubscription = parentCancelSubscription;
        Description = description;
        this.ID = ID;
        Grandtotal = grandtotal;
        Payments = payments;
        TotalWeivered = totalWeivered;
        Balances = balances;
        Status = status;
    }

    public int getSId() {
        return SId;
    }

    public String getSUID() {
        return SUID;
    }

    public String getName() {
        return Name;
    }

    public String getAdmno() {
        return Admno;
    }

    public String getSEX() {
        return SEX;
    }

    public int getClassID() {
        return ClassID;
    }

    public String getCName() {
        return CName;
    }

    public Integer getStreamID() {
        return StreamID;
    }

    public String getStreamName() {
        return StreamName;
    }

    public String getParentName() {
        return ParentName;
    }

    public String getDOB() {
        return DOB;
    }

    public String getParentID() {
        return ParentID;
    }

    public String getParentCellNo() {
        return ParentCellNo;
    }

    public String getParent2Name() {
        return Parent2Name;
    }

    public String getParent2Phone() {
        return Parent2Phone;
    }

    public String getGuardianName() {
        return GuardianName;
    }

    public String getGuardianPhone() {
        return GuardianPhone;
    }

    public String getGuardianRelationship() {
        return GuardianRelationship;
    }

    public String getGuardianOccupation() {
        return GuardianOccupation;
    }

    public String getHome() {
        return Home;
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public String getHomeLocation() {
        return HomeLocation;
    }

    public Boolean getActive() {
        return Active;
    }

    public String getTrxDate() {
        return TrxDate;
    }

    public String getRegisteredOn() {
        return RegisteredOn;
    }

    public String getFilter() {
        return Filter;
    }

    public String getTransportPackageId() {
        return TransportPackageId;
    }

    public String getTransportPackage() {
        return TransportPackage;
    }

    public Integer getHasSubscription() {
        return HasSubscription;
    }

    public String getPackageId() {
        return PackageId;
    }

    public String getPackageName() {
        return PackageName;
    }

    public String getUserName() {
        return UserName;
    }

    public String getSubScriptionNextDate() {
        return SubScriptionNextDate;
    }

    public String getSubScriptionStartDate() {
        return SubScriptionStartDate;
    }

    public String getSubScriptionDate() {
        return SubScriptionDate;
    }

    public Boolean getActiveSubscription() {
        return ActiveSubscription;
    }

    public Boolean getParentCancelSubscription() {
        return ParentCancelSubscription;
    }

    public String getDescription() {
        return Description;
    }

    public String getID() {
        return ID;
    }

    public String getGrandtotal() {
        return Grandtotal;
    }

    public String getPayments() {
        return Payments;
    }

    public String getTotalWeivered() {
        return TotalWeivered;
    }

    public String getBalances() {
        return Balances;
    }

    public String getStatus() {
        return Status;
    }

    @Override
    public String toString() {
        return "StudentsRespModel{" +
                "SId=" + SId +
                ", SUID='" + SUID + '\'' +
                ", Name='" + Name + '\'' +
                ", Admno='" + Admno + '\'' +
                ", SEX='" + SEX + '\'' +
                ", ClassID=" + ClassID +
                ", CName='" + CName + '\'' +
                ", StreamID=" + StreamID +
                ", StreamName='" + StreamName + '\'' +
                ", ParentName='" + ParentName + '\'' +
                ", DOB='" + DOB + '\'' +
                ", ParentID='" + ParentID + '\'' +
                ", ParentCellNo='" + ParentCellNo + '\'' +
                ", Parent2Name='" + Parent2Name + '\'' +
                ", Parent2Phone='" + Parent2Phone + '\'' +
                ", GuardianName='" + GuardianName + '\'' +
                ", GuardianPhone='" + GuardianPhone + '\'' +
                ", GuardianRelationship='" + GuardianRelationship + '\'' +
                ", GuardianOccupation='" + GuardianOccupation + '\'' +
                ", Home='" + Home + '\'' +
                ", HomeAddress='" + HomeAddress + '\'' +
                ", HomeLocation='" + HomeLocation + '\'' +
                ", Active=" + Active +
                ", TrxDate='" + TrxDate + '\'' +
                ", RegisteredOn='" + RegisteredOn + '\'' +
                ", Filter='" + Filter + '\'' +
                ", TransportPackageId='" + TransportPackageId + '\'' +
                ", TransportPackage='" + TransportPackage + '\'' +
                ", HasSubscription=" + HasSubscription +
                ", PackageId='" + PackageId + '\'' +
                ", PackageName='" + PackageName + '\'' +
                ", UserName='" + UserName + '\'' +
                ", SubScriptionNextDate='" + SubScriptionNextDate + '\'' +
                ", SubScriptionStartDate='" + SubScriptionStartDate + '\'' +
                ", SubScriptionDate='" + SubScriptionDate + '\'' +
                ", ActiveSubscription=" + ActiveSubscription +
                ", ParentCancelSubscription=" + ParentCancelSubscription +
                ", Description='" + Description + '\'' +
                ", ID='" + ID + '\'' +
                ", Grandtotal='" + Grandtotal + '\'' +
                ", Payments='" + Payments + '\'' +
                ", TotalWeivered='" + TotalWeivered + '\'' +
                ", Balances='" + Balances + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }
}


