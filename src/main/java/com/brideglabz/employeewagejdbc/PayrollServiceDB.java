package com.brideglabz.employeewagejdbc;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class PayrollServiceDB {
    private PreparedStatement preparedStatement;

    public Connection getConnection() throws EmployeePayrollException {
        String jdbcURL = "jdbc:mysql://localhost:3306/employee_payroll_?useSSL=false";
        String userName = "root";
        String password = "Usha@1234";
        Connection connection = null;
        try {
            System.out.println("Connecting to database:" + jdbcURL);
            connection = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection is successful!" + connection);

        } catch (SQLException e) {
            e.printStackTrace();
            //throw new EmployeePayrollException("Unable to connect / Wrong Entry");
        }
        return connection;
    }

    private static void listDrivers() {
        Enumeration<Driver> driverList = DriverManager.getDrivers();
        while (driverList.hasMoreElements()) {
            Driver driverClass = (Driver) driverList.nextElement();
            System.out.println("Driver:" + driverClass.getClass().getName());
        }
    }

    public List<EmployeePayrollData> readData() throws EmployeePayrollException {
        String sql = "SELECT * FROM employee_payroll;";
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("basic_pay");
                LocalDate startDate = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary, startDate));
            }
            connection.close();
        } catch (SQLException e) {
            throw new EmployeePayrollException("Unable to Retrieve data From Table!");
        }
        return employeePayrollList;
    }


    public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws EmployeePayrollException {
        String sql = String.format("SELECT * FROM employee_payroll WHERE name='%s'", name);
        List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String objectname = resultSet.getString("name");
                double salary = resultSet.getDouble("basic_pay");
                LocalDate start = resultSet.getDate("start").toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, objectname, salary, start));
            }
            return employeePayrollList;
        } catch (SQLException e) {
            throw new EmployeePayrollException("Unable to get data from database");
        }
    }

    public int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollException {
        String sql = String.format("UPDATE employee_payroll SET salary=%.2f WHERE name='%s'", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql);
            return rowsAffected;
        } catch (SQLException e) {
            throw new EmployeePayrollException("Unable To update data in database");
        }
    }

    public int updateEmployeePayrollDataUsingPreparedStatement(String name, double salary)
            throws EmployeePayrollException {
        if (this.preparedStatement == null) {
            this.prepareStatementForEmployeePayroll();
        }
        try {
            preparedStatement.setDouble(1, salary);
            preparedStatement.setString(2, name);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected;
        } catch (SQLException e) {
            throw new EmployeePayrollException("Unable to use prepared statement");
        }
    }

    private void prepareStatementForEmployeePayroll() throws EmployeePayrollException {
        try {
            Connection connection = this.getConnection();
            String sql = "UPDATE employee_payroll SET salary=? WHERE name=?";
            this.preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Unable to prepare statement");
        }
    }

    public List<EmployeePayrollData> getEmployeePayrollDataByStartingDate(LocalDate startDate, LocalDate endDate) throws EmployeePayrollException {
        String sql = String.format(
                "SELECT * FROM employee_payroll WHERE start BETWEEN cast('%s' as date) and cast('%s' as date);",
                startDate.toString(), endDate.toString());
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return this.getEmployeePayrollListFromResultset(resultSet);
        } catch (SQLException e) {
            throw new EmployeePayrollException("Connection Failed.");
        }
    }

    public Map<String, Double> performAverageAndMinAndMaxOperations(String column, String operation)
            throws EmployeePayrollException {

        String sql = String.format("SELECT gender,%s(%s) FROM employee_payroll GROUP BY gender;", operation, column);
        Map<String, Double> mapValues = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                mapValues.put(resultSet.getString(1), resultSet.getDouble(2));
            }
        } catch (SQLException e) {
            throw new EmployeePayrollException("Connection Failed.");
        }
        return mapValues;
    }

    public EmployeePayrollData addEmployeeToPayroll(String name, String gender, double salary, LocalDate startDate)
            throws EmployeePayrollException {
        int employeeId = -1;
        EmployeePayrollData employeePayrollData = null;
        String sql = String.format(
                "INSERT INTO employee_payroll(name,gender,salary,start) " + "VALUES ( '%s', '%s', %s, '%s' )", name,
                gender, salary, Date.valueOf(startDate));
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowsAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    employeeId = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData(employeeId, name, gender, salary, startDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeePayrollData;
    }
}