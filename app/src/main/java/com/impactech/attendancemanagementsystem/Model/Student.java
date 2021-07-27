package com.impactech.attendancemanagementsystem.Model;

public class Student extends Person{
    private String department, bluetoothId, regNo;

    public Student(String regNo, String surname, String firstName, String otherNames, String department, String bluetoothId) {
        super(surname, firstName, otherNames);
        this.department = department;
        this.bluetoothId = bluetoothId;
        this.regNo = regNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBluetoothId() {
        return bluetoothId;
    }

    public void setBluetoothId(String bluetoothId) {
        this.bluetoothId = bluetoothId;
    }
}
