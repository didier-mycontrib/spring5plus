package tp.mySpringBatch.reader.java;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")
public class MyXmlFilePersonReaderConfig {
	
	 @Value("file:data/input/xml/inputData.xml") //to read in project root directory
	  //NB: by default @Value(path) is @Value("classpath:path) //to read in src/main/resource or other classpath part
	  private Resource inputXmlResource;
	 
	 /*
	 //V1 without builder:
	  @Bean @Qualifier("xml")
	  ItemReader<Person> personXmlFileItemReader() {

		  //Create writer instance
		  StaxEventItemReader<Person> itemReader = 
		          new StaxEventItemReader<Person>();
		  
		  var personXmlMarshaller = new Jaxb2Marshaller();
		  personXmlMarshaller.setClassesToBeBound(new Class[] { Person.class });
		  itemReader.setFragmentRootElementName("person");
		  itemReader.setUnmarshaller(personXmlMarshaller);
		  itemReader.setResource(inputXmlResource);
	      return itemReader;
		}
		*/
	 
	 //V2 with builder:
	 @Bean(destroyMethod="") @Qualifier("xml")
	  ItemReader<Person> personXmlFileItemReader() {

		 var personXmlMarshaller = new Jaxb2Marshaller();
		  personXmlMarshaller.setClassesToBeBound(new Class[] { Person.class });
		  
		  return new StaxEventItemReaderBuilder<Person>() 
		  .name("personXmlFileItemReader")
		  .addFragmentRootElements("person")
		  .unmarshaller(personXmlMarshaller)
		  .resource(inputXmlResource)
	      .build();
		}
}
