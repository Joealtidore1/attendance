package com.impactech.attendancemanagementsystem.Model;

public class Lecturer extends Person{

    String staffId;
    String designation;
    String relatedFields;
    String department;
    String role;
    String password;
    String discipline;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }


    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getRelatedFields() {
        return relatedFields;
    }

    public void setRelatedFields(String relatedFields) {
        this.relatedFields = relatedFields;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Lecturer(String staffId,
                    String surname,
                    String firstName,
                    String otherNames,
                    String designation,
                    String discipline,
                    String relatedFields,
                    String department,
                    String role,
                    String password) {
        super(surname, firstName, otherNames);
        this.staffId = staffId;
        this.designation = designation;
        this.discipline = discipline;
        this.relatedFields = relatedFields;
        this.department = department;
        this.role = role;
        this.password = password;
    }
}
