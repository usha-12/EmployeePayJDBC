package com.brideglabz.employeewagejdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class EmployeePayrollServiceTest {
    @Test
    public <EmployeePayrollData> void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData;
        employeePayrollData = (List<EmployeePayrollData>) employeePayrollService.readEmployeePayrollData();
        Assertions.assertEquals(employeePayrollData.size(),5);
    }


    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData;
        employeePayrollData = employeePayrollService.readEmployeePayrollData();
        employeePayrollService.updateEmployeeSalary("nagraj", 3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("nagraj");
    }

    @Test
    public void givenEmployeePayroll_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData;
        employeePayrollData =employeePayrollService.readEmployeePayrollData();
        employeePayrollService.updateEmployeeSalary("Nagraj",3000000.00);
        boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Nagraj");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenEmployeePayrollData_WhenRetrievedBasedOnStartDate_ShouldReturnResult() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData();
        LocalDate startDate = LocalDate.parse("2018-01-01");
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> matchingRecords = employeePayrollService.getEmployeePayrollDataByStartDate(startDate,endDate);
        Assertions.assertEquals(matchingRecords.get(0), employeePayrollService.getEmployeePayrollData("Harry"));
    }


    @Test
    public void givenEmployee_PerformedVariousOperations_ShouldGiveResult() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData();
        Map<String, Double> maxSalaryByGender = employeePayrollService.performOperationByGender("salary", "MAX");
        Assertions.assertEquals(3000000.0, maxSalaryByGender.get("F"), 0.0);
    }


    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData();
        employeePayrollService.addEmployeeToPayroll("Mark","M", 5000000.00, LocalDate.now());
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
        Assertions.assertTrue(result);
    }


}
