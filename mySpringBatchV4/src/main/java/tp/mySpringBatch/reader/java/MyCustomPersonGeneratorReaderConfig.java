package tp.mySpringBatch.reader.java;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tp.mySpringBatch.model.Employee;
import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.reader.custom.EmployeeGeneratorReader;
import tp.mySpringBatch.reader.custom.PersonGeneratorReader;



@Configuration
public class MyCustomPersonGeneratorReaderConfig {
	
	  @Bean @StepScope @Qualifier("fromMyGeneratorPersonReader")
	  public ItemReader<Person> fromMyGeneratorPersonReader(
			  @Value("#{jobParameters[dataSetSize]}") Long dataSetSize
			  ) {
		  if(dataSetSize==null)
			  dataSetSize=100L;
		  return new PersonGeneratorReader(dataSetSize);
	
	  }
	  
	  @Bean @StepScope @Qualifier("fromMyGeneratorEmployeeReader")
	  public ItemReader<Employee> fromMyGeneratorEmployeeReader(
			  @Value("#{jobParameters[dataSetSize]}") Long dataSetSize
			  ) {
		  if(dataSetSize==null)
			  dataSetSize=100L;
		  return new EmployeeGeneratorReader(dataSetSize);

	  }

}
