package tp.mySpringBatch.reader.java;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tp.mySpringBatch.db.EmployeeRowMapper;
import tp.mySpringBatch.db.EmployeeStatRowMapper;
import tp.mySpringBatch.db.PersonRowMapper;
import tp.mySpringBatch.model.Employee;
import tp.mySpringBatch.model.EmployeeStat;
import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")
public class MyDbPersonReaderConfig {
	
	private static final String SELECT_QUERY = "SELECT id, first_name, last_name, age, is_active FROM person"	;
	private static final String SELECT_QUERY_EMPLOYEE = "SELECT p.id, p.first_name, p.last_name, p.age, p.is_active, f.function, f.salary FROM person p LEFT JOIN functions f ON p.id=f.id"	;
	private static final String SELECT_QUERY_EMPLOYEE_STAT = """
			SELECT f.function, count(f.id) as number_of_employees , min(f.salary) as min_salary , max(f.salary) as max_salary , round( avg(f.salary) , 2) as average_salary 
			FROM person p LEFT JOIN functions f ON p.id=f.id 
			WHERE p.is_active = true 
			GROUP BY f.function"""	;
	
	
	 @Bean @Qualifier("db")
	  ItemReader<Person> jdbcPersonReader(@Qualifier("inputdb") DataSource inputdbDataSource) {
			 return new JdbcCursorItemReaderBuilder<Person>()
			  .name("jdbcPersonReader")
			  .dataSource(inputdbDataSource)
			  .sql(SELECT_QUERY)
			  .rowMapper(new PersonRowMapper())
			  .build();
		
		}
	 

	@Bean @Qualifier("db")
		 ItemReader<Employee> jdbcEmployeeReader(@Qualifier("inputdb") DataSource inputdbDataSource) {
			return new JdbcCursorItemReaderBuilder<Employee>()
			.name("jdbcEmployeeReader")
			.dataSource(inputdbDataSource)
			.sql(SELECT_QUERY_EMPLOYEE)
			.rowMapper(new EmployeeRowMapper())
			.build();
			
		}
	
	@Bean @Qualifier("db")
	 ItemReader<EmployeeStat> jdbcEmployeeStatReader(@Qualifier("inputdb") DataSource inputdbDataSource) {
		return new JdbcCursorItemReaderBuilder<EmployeeStat>()
		.name("jdbcEmployeeStatReader")
		.dataSource(inputdbDataSource)
		.sql(SELECT_QUERY_EMPLOYEE_STAT)
		.rowMapper(new EmployeeStatRowMapper())
		.build();
		
	}
	 
	 
	 //TEMPORAIRE(premier lancement) pour préparer (créer) la table et un jeu de données dans la base inputdb
	 //old inputDatabaseInitializer() now replaced by db.MyInputDbInitializer
	
}
