package com.example.studentescortsystem.school;

public class ReadWriteSchoolDetails {
    public String school,pincode,mobile;


    //constructor
    public ReadWriteSchoolDetails(){};
    public ReadWriteSchoolDetails(String textSchoolName,String textPincode,String textMobile){
        this.school=textSchoolName;
        this.pincode=textPincode;
        this.mobile=textMobile;
    }
}
