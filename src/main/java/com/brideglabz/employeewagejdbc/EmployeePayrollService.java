package com.brideglabz.employeewagejdbc;

import java.util.List;

public class EmployeePayrollService {
    public PayrollServiceDB payrollServiceDB;

    public EmployeePayrollService() {
        super();
        this.payrollServiceDB = new PayrollServiceDB();
    }

    public List<EmployeePayrollData> readEmployeePayrollData() throws EmployeePayrollException {
        return this.payrollServiceDB.readData();
    }
}
