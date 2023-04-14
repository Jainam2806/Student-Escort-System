package com.example.studentescortsystem.parent;

public class ReadWriteUserDetails {
    public String school,rollNo,doB, gender ,mobile;


    //constructor
    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String textSchoolName,String textRollNo,String textDoB,String textGender,String textMobile){
        this.school=textSchoolName;
        this.rollNo=textRollNo;
        this.doB=textDoB;
        this.gender=textGender;
        this.mobile=textMobile;
    }
}
