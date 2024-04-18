package tp.mySpringBatch.writer.java;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tp.mySpringBatch.model.Employee;
import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.writer.custom.NewEmployeeDbWriter;

@Configuration
@Profile("!xmlJobConfig")
public class MyDbPersonWriterConfig {

	  
	private static final String INSERT_QUERY = """
	      insert into person (first_name, last_name, age, is_active)
	      values (:firstName,:lastName,:age,:active)""";
	
	private static final String UPDATE_FUNCTIONS_QUERY = """
		UPDATE functions SET function = :function , salary = :salary WHERE id = :id """;
	 

	/*
      //V1 (sans builder):
	  @Bean @Qualifier("db")
	  public JdbcBatchItemWriter<Person> jdbcItemWriter(@Qualifier("outputdb") DataSource outputdbDataSource) {
	    var provider = new BeanPropertyItemSqlParameterSourceProvider<Person>();
	    var itemWriter = new JdbcBatchItemWriter<Person>();
	    itemWriter.setDataSource(outputdbDataSource);
	    itemWriter.setSql(INSERT_QUERY);
	    itemWriter.setItemSqlParameterSourceProvider(provider);
	    return itemWriter;
	  }
	  */
	 
	 //V2: avec builder
	 @Bean @Qualifier("db")
	  public JdbcBatchItemWriter<Person> jdbcItemWriter(@Qualifier("outputdb") DataSource outputdbDataSource) {
		 return new JdbcBatchItemWriterBuilder<Person>()
		  .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>())
		  .dataSource(outputdbDataSource)
		  .sql(INSERT_QUERY)
		  .build();
	  }
	 
	 @Bean @Qualifier("update_db")
	  public ItemWriter<Employee> employeeFunctionsPartJdbcItemWriter(@Qualifier("inputdb") DataSource outputdbDataSource) {
		 return new JdbcBatchItemWriterBuilder<Employee>()
				  .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>())
				  .dataSource(outputdbDataSource)
				  .sql(UPDATE_FUNCTIONS_QUERY)
				  .build();
	  }
	 
	 @Bean @Qualifier("generate_db")
	  public JdbcBatchItemWriter<Person> genJdbcItemWriter(@Qualifier("inputdb") DataSource outputdbDataSource) {
		 return new JdbcBatchItemWriterBuilder<Person>()
		  .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>())
		  .dataSource(outputdbDataSource)
		  .sql(INSERT_QUERY)
		  .build();
	  }
	 
	 @Bean @Qualifier("generate_db")
	  public ItemWriter<Employee> genEmployeeJdbcItemWriter(@Qualifier("inputdb") DataSource outputdbDataSource) {
		 return new NewEmployeeDbWriter(outputdbDataSource);
	  }
	
}
