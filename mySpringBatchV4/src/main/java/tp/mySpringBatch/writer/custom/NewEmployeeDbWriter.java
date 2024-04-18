package tp.mySpringBatch.writer.custom;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import tp.mySpringBatch.model.Employee;

public class NewEmployeeDbWriter implements ItemWriter<Employee>{
	
	private DataSource dataSource;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public NewEmployeeDbWriter() {
	}
	
	private final String INSERT_PERSON_SQL = "INSERT INTO person (first_name, last_name, age, is_active) VALUES (:firstName,:lastName,:age,:active)";
	private final String INSERT_FUNCTIONS_SQL = "INSERT INTO functions(id, function,salary) VALUES(:id,:function,:salary)";
	
	public Employee insertPersonPartOfEmployee(Employee emp) {
		KeyHolder holder = new GeneratedKeyHolder();
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("firstName", emp.getFirstName())
				.addValue("lastName", emp.getLastName())
				.addValue("age", emp.getAge())
				.addValue("active", emp.getActive());
		namedParameterJdbcTemplate.update(INSERT_PERSON_SQL, parameters, holder);
		emp.setId(holder.getKey().longValue());//store auto_increment pk in instance to return
		return emp;
	}
	
	public void insertFunctionsPartOfEmployee(Employee emp) {
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("id", emp.getId())
				.addValue("function", emp.getFunction())
				.addValue("salary", emp.getSalary());
		namedParameterJdbcTemplate.update(INSERT_FUNCTIONS_SQL, parameters);
	}

	@Override
	public void write(List<? extends Employee> chunk) throws Exception {
		// write a list of Employee in 2 tables of the database : person and functions
		for(Employee emp : chunk) {
			emp=insertPersonPartOfEmployee(emp);
			insertFunctionsPartOfEmployee(emp);
		}
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(dataSource);
	}

	public NewEmployeeDbWriter(DataSource dataSource) {
		super();
		this.setDataSource(dataSource);
	}
	
}
