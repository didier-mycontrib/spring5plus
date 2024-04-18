package tp.mySpringBatch.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import tp.mySpringBatch.model.Employee;

public class EmployeeRowMapper implements RowMapper<Employee> {

    public static final String FUNCTION_COLUMN = "function";
    public static final String SALARY_COLUMN = "salary";

    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee emp = new Employee();
        emp.setId(rs.getLong(PersonRowMapper.ID_COLUMN));
        emp.setFirstName(rs.getString(PersonRowMapper.FIRSTNAME_COLUMN));
        emp.setLastName(rs.getString(PersonRowMapper.LASTNAME_COLUMN));
        emp.setAge(rs.getInt(PersonRowMapper.AGE_COLUMN));
        emp.setActive(rs.getBoolean(PersonRowMapper.ACTIVE_COLUMN));
        emp.setFunction(rs.getString(FUNCTION_COLUMN));
        emp.setSalary(rs.getDouble(SALARY_COLUMN));

        return emp;
    }
}
