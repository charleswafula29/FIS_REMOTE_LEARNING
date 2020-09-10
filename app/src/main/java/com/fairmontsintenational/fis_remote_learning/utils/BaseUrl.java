package com.fairmontsintenational.fis_remote_learning.utils;

import com.androidstudy.daraja.util.Settings;

import io.paperdb.Paper;

import static com.fairmontsintenational.fis_remote_learning.utils.Utils.getCurrentDateTime;

public class BaseUrl {
    public static final String GET_MAIN_URL = "https://www.fairmontsinternationalschool.co.ke/fairmontsAPI/url.php";
    private static String main = "http://197.248.111.170:56/";
    public static String Passkey = "79b5e1fd74b971e4f20901005e79928a23dbaf5ac313f25fd78ef0ce874a19b1";
    public static String BusinessShortCode = "4030607";
    public static String callbackurl = "http://fairmontsinternationalschool.co.ke/RemotePayments/callBack_url.php";
    public static String transctionDesc = "Fees Payment";
    public static final String DARAJA_CONSUMER_KEY="KxJyplEHiwFKoFz8gFHasHuxHAgVX6Ym";
    public static final String  DARAJA_CONSUMER_SECRET="Qh1k5QWBCF3DDc0J";
    public static final String  REG_AMOUNT="2000";
    public static final String  PSA = "Pending Student Activation";
    public static final String  HNS = "Has No Subscription";
    public static final String  IAAP = "In Arrears Awaiting Payment";
    public static final String  SAP = "Suspended Awaiting Payment";
    public static final String  SCBP = "Subscription cancelled by parent";
    public static final String  DEAC = "Deactivated";
    public static final String  Active = "Active";
    public static final String  RegistrationCharges = "RegistrationCharges";
    public static final String  OnlinePackage = "OnlinePackage";

    public static String getLogin(String phone,String pass){
        return main+"api/OnlineLogin?phone_no="+phone+"&password="+pass;
    }

    public static String getRegister(){
        return main+"api/ManageParent";
    }

    public static String getRegisterStudent(){
        return main+"api/ManageStudent";
    }

    public static String getUploadParentPic(){
        return main+"api/PostParentProfilePicture";
    }

    public static String getStudentCharges(){
        return main+"api/GetCharges";
    }

    public static String getChargeStudent(){
        return main+"api/ChargeStudent";
    }

    public static String getStudentLevels(){
        return main+"api/GetLevels";
    }

    public static String getParentProfPic(String parentId){
        return main+"api/ParentProfilePicture?ParentID="+parentId;
    }

    public static String getFeeStatement(String sid){
        return main+"api/GetFeeStatement?Sid="+sid.replace("SID","")+"&asDate=1/1/2020";
    }

    public static String getFetchStudentPic(Integer sid){
        return main+"api/StudentProfilePicture?Sid="+sid;
    }

    public static String getStudentCredentials(Integer sid){
        return main+"api/VirtualCredentials?sid="+sid;
    }

    public static String getUploadStudentPic(){
        return main+"api/UpdateStudentProfilePic";
    }

    public static String getStudentReports(Integer sid){
        return main+"api/fetchreportsSId?Sid="+sid;
    }

    public static String getCommentStudentReports(Integer diaryID,String Date,String Comment){
        return main+"api/addcomment?diary_id="+diaryID+"&date="+Date+"&comment="+Comment;
    }

    public static String getCancelSubscription(){
        return main+"api/CancelStudentSubScription";
    }

    public static String getRequestResetCode(String phoneNo){
        return main+"api/GenerateResetPasswordCode?phone_no="+phoneNo+"&ExpiryTime="+getCurrentDateTime();
    }
    public static String getChangePassword(String phoneNo,String newPass){
        return main+"api/ForgotPassword?phone_no="+phoneNo+"&new_password="+newPass;
    }


}
