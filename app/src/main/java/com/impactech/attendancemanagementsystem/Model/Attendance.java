package com.impactech.attendancemanagementsystem.Model;

public class Attendance {
    private String code, regNo, session, date, staffId;

    public Attendance(String code, String staffId, String regNo, String session, String date) {
        this.code = code;
        this.regNo = regNo;
        this.session = session;
        this.date = date;
        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
