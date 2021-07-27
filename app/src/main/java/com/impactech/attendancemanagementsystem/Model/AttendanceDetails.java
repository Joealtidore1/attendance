package com.impactech.attendancemanagementsystem.Model;

public class AttendanceDetails {
    String regNo, name, attPer;
    int numOfAtt;

    public AttendanceDetails(String regNo, String name, String attPer, int numOfAtt) {
        this.regNo = regNo;
        this.name = name;
        this.attPer = attPer;
        this.numOfAtt = numOfAtt;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttPer() {
        return attPer;
    }

    public void setAttPer(String attPer) {
        this.attPer = attPer;
    }

    public int getNumOfAtt() {
        return numOfAtt;
    }

    public void setNumOfAtt(int numOfAtt) {
        this.numOfAtt = numOfAtt;
    }
}
