package tp.mySpringBatch.reader.java;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tp.mySpringBatch.model.Person;



@Configuration
public class MyMemoryJobContextPersonReaderConfig {
	

	  @Bean @Qualifier("fromJobExecutionContext")
	  public ItemReader<Person> fromInMemoryJobContextPersonReader() {
		  return new FromJobExecutionContextReader<Person>();
	
	  }

}
