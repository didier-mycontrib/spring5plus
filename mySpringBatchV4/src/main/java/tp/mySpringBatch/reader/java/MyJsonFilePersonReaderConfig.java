package tp.mySpringBatch.reader.java;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")
public class MyJsonFilePersonReaderConfig {
	
	 @Value("file:data/input/json/inputData.json") //to read in project root directory
	  //NB: by default @Value(path) is @Value("classpath:path) //to read in src/main/resource or other classpath part
	  private Resource inputJsonResource;
	 
	 
	 //with builder:
	 @Bean(destroyMethod="") @Qualifier("json")
	  ItemReader<Person> personJsonFileItemReader() {
		 return new JsonItemReaderBuilder<Person>()
				 .name("personJsonFileItemReader")
                 .jsonObjectReader(new JacksonJsonObjectReader<Person>(Person.class))
                 .resource(inputJsonResource)
                 .build();
		}
}
