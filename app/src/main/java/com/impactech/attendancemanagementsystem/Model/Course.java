package com.impactech.attendancemanagementsystem.Model;

public class Course {
    private String code, title, semester, dept, creditUnits;

    public Course(String code, String title, String semester, String dept, String creditUnits) {
        this.code = code;
        this.title = title;
        this.semester = semester;
        this.dept = dept;
        this.creditUnits = creditUnits;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCreditUnits() {
        return creditUnits;
    }

    public void setCreditUnits(String creditUnits) {
        this.creditUnits = creditUnits;
    }
}
