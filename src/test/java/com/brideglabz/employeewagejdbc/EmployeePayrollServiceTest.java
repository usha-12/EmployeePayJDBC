package com.brideglabz.employeewagejdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class EmployeePayrollServiceTest {
    @Test
    public <EmployeePayrollData> void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws EmployeePayrollException {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData;
        employeePayrollData = (List<EmployeePayrollData>) employeePayrollService.readEmployeePayrollData();
        Assertions.assertEquals(employeePayrollData.size(),5);
    }

}
