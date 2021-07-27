package com.impactech.attendancemanagementsystem.Model;

public class CourseAssignment {
    String code, staffId, session;

    public CourseAssignment(String code, String staffId, String session) {
        this.code = code;
        this.staffId = staffId;
        this.session = session;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
