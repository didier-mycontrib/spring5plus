package tp.mySpringBatch.writer.java;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.WritableResource;

import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.model.PersonWithNumAndAddress;

@Configuration
@Profile("!xmlJobConfig")
public class MyJsonFilePersonWriterConfig {


	  @Value("file:data/output/json/outputData.json") //to read in project root directory
	  //NB: by default @Value(path) is @Value("classpath:path) //to read in src/main/resource or other classpath part
	  private WritableResource outputJsonResource;
	 
	  @Value("file:data/output/json/personWithNumberAndAddress.json") //to read in project root directory
	  private WritableResource personWithNumAndAddressOutputJsonResource;
	  
	  //avec builder:
	  @Bean @Qualifier("json")
	  ItemWriter<Person> personJsonFileItemWriter() {		  
		  return new JsonFileItemWriterBuilder<Person>()
				     .name("personJsonFileItemWriter")
	                 .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
	                 .resource(outputJsonResource)
	                 .build();
		}
	  
	  @Bean @Qualifier("json")
	  ItemWriter<PersonWithNumAndAddress> personWithNumAndAddressJsonFileItemWriter() {		  
		  return new JsonFileItemWriterBuilder<PersonWithNumAndAddress>()
				     .name("personWithNumAndAddressJsonFileItemWriter")
	                 .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
	                 .resource(personWithNumAndAddressOutputJsonResource)
	                 .build();
		}
}
