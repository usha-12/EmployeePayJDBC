package com.brideglabz.employeewagejdbc;

public class EmployeePayrollData {
    private int id;
    private String name;
    private String gender;
    private String city;

    public EmployeePayrollData(int id, String name, String gender, String city) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.city = city;
    }

    @Override
    public String toString() {
        return "EmployeePayrollData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
