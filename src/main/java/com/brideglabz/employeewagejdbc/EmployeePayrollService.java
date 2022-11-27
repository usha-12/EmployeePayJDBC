package com.brideglabz.employeewagejdbc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EmployeePayrollService {
    public List<EmployeePayrollData> employeePayrollList;
    private PayrollServiceDB payrollServiceDB;

    public EmployeePayrollService() {
        super();
        this.payrollServiceDB = new PayrollServiceDB();
    }

    public List<EmployeePayrollData> readEmployeePayrollData() throws EmployeePayrollException {
        this.employeePayrollList = this.payrollServiceDB.readData();
        return this.employeePayrollList;
    }
    public void updateEmployeeSalary(String name, double salary) throws EmployeePayrollException {
        int result = new PayrollServiceDB().updateEmployeePayrollDataUsingPreparedStatement(name, salary);
        if (result == 0)
            return;
        EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
        if (employeePayrollData != null)
            employeePayrollData.setSalary(salary);
    }

    EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollList.stream()
                .filter(employeePayrollObject -> employeePayrollObject.getName().equals(name)).findFirst().orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollException {
        List<EmployeePayrollData> employeePayrollDataList = new PayrollServiceDB().getEmployeePayrollDataFromDB(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
    }

    public List<EmployeePayrollData> getEmployeePayrollDataByStartDate(LocalDate startDate, LocalDate endDate)
            throws EmployeePayrollException {
        return this.payrollServiceDB.getEmployeePayrollDataByStartingDate(startDate, endDate);
    }

    public Map<String, Double> performOperationByGender(String column, String operation)
            throws EmployeePayrollException {
        return this.payrollServiceDB.performAverageAndMinAndMaxOperations(column, operation);
    }

    public void addEmployeeToPayroll(String name, String gender, double salary, LocalDate startDate)
            throws EmployeePayrollException {
        employeePayrollList.add(payrollServiceDB.addEmployeeToPayroll(name, gender, salary, startDate));
    }
}
