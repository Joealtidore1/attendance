package com.impactech.attendancemanagementsystem.Model;

public class Enrollment {
    String code, regNo, session;

    public Enrollment(String code, String regNo, String session) {
        this.code = code;
        this.regNo = regNo;
        this.session = session;
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
}
