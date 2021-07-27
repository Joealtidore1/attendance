package com.impactech.attendancemanagementsystem.Model;

public class Person {
    String surname;
    String firstName;
    String otherNames;

    public Person(String surname, String firstName, String otherNames) {
        this.surname = surname;
        this.firstName = firstName;
        this.otherNames = otherNames;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }
}
