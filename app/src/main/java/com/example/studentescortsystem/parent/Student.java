package com.example.studentescortsystem.parent;

public class Student {
    String textFullName;
    String textEmail;
    String textDoB;
    String textMobile;
    String textRollNo;
    String textSchoolName ;
    String textGender;

    public Student() {
        // Required empty constructor for Firestore
    }

    public Student(String textFullName, String textEmail, String textDoB, String textMobile, String textRollNo, String textSchoolName , String textGender) {
        this.textFullName=textFullName;
        this.textEmail=textEmail;
        this.textDoB=textDoB;
        this.textMobile=textMobile;
        this.textRollNo=textRollNo;
        this.textSchoolName=textSchoolName;
        this.textGender=textGender;
    }
}
