package com.brideglabz.employeewagejdbc;

import java.util.Arrays;
import java.util.List;

public class EmployeePayrollService {
    public PayrollServiceDB payrollServiceDB;
    private Arrays employeePayrollList;

    public EmployeePayrollService() {
        super();
        this.payrollServiceDB = new PayrollServiceDB();
    }

    public List<EmployeePayrollData> readEmployeePayrollData() throws EmployeePayrollException {
        return this.payrollServiceDB.readData();
    }

    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = new PayrollServiceDB().updateEmployeeDataUsingStatement(name, salary);
        if (result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            employeePayrollData.setSalary(salary);
    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollObject -> employeePayrollObject.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollDataList = new PayrollServiceDB().getEmployeePayrollDataFromDB(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }
}
