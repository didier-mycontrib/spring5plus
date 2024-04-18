package tp.mySpringBatch.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import tp.mySpringBatch.model.EmployeeStat;

public class EmployeeStatRowMapper implements RowMapper<EmployeeStat> {

    public static final String FUNCTION_COLUMN = "function";
    public static final String NUMBER_OF_EMPLOYEES_COLUMN = "number_of_employees";
    public static final String MIN_SALARY_COLUMN = "min_salary";
    public static final String MAX_SALARY_COLUMN = "max_salary";
    public static final String AVERAGE_SALARY_COLUMN = "average_salary";

    public EmployeeStat mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmployeeStat empStat = new EmployeeStat();
        empStat.setNumberOfEmployees(rs.getLong(NUMBER_OF_EMPLOYEES_COLUMN));
        empStat.setFunction(rs.getString(FUNCTION_COLUMN));
        empStat.setMinSalary(rs.getDouble(MIN_SALARY_COLUMN));
        empStat.setMaxSalary(rs.getDouble(MAX_SALARY_COLUMN));
        empStat.setAverageSalary(rs.getDouble(AVERAGE_SALARY_COLUMN));
        return empStat;
    }
}
