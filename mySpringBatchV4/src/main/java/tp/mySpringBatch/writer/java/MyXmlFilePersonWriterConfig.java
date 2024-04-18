package tp.mySpringBatch.writer.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")
public class MyXmlFilePersonWriterConfig { 
	
	public static final Logger logger = LoggerFactory.getLogger(MyXmlFilePersonWriterConfig.class);


	  @Value("file:data/output/xml/outputData.xml") //to read in project root directory
	  //NB: by default @Value(path) is @Value("classpath:path) //to read in src/main/resource or other classpath part
	  private WritableResource outputXmlResource;
	 
	  /*
	  //V1 sans builder
	  @Bean @Qualifier("xml")
	  ItemStreamWriter<Person> personXmlFileItemWriter() {

		  //Create writer instance
		  StaxEventItemWriter<Person> itemWriter = 
		          new StaxEventItemWriter<Person>();
		  
		  var personXmlMarshaller = new Jaxb2Marshaller();
		  personXmlMarshaller.setClassesToBeBound(new Class[] { Person.class });

		  itemWriter.setMarshaller(personXmlMarshaller);
	      itemWriter.setRootTagName("persons");
	      itemWriter.setResource(outputXmlResource);
	      return itemWriter;
		}
		*/
	  
	  //V2 avec builder:
	  @Bean(destroyMethod="") @Qualifier("xml")
	  @StepScope
	  ItemStreamWriter<Person> personXmlFileItemWriter(
			 @Value("#{jobParameters['msg1']}") String msg1
			  ) {
		  
		//just to see that @Value("#{jobParameters['msg1']}")
		//can be used in @JobScope or @StepScope ItemWriter @Bean
		logger.info("in @Bean personXmlFileItemWriter() , msg1="+msg1);
		
			  
		  var personXmlMarshaller = new Jaxb2Marshaller();
		  personXmlMarshaller.setClassesToBeBound(new Class[] { Person.class });
		  
		  return new StaxEventItemWriterBuilder<Person>()
		  .name("personXmlFileItemWriter")
		  .marshaller(personXmlMarshaller)
	      .rootTagName("persons")
	      .resource(outputXmlResource)
	      .build();
		}
	  
	  
	
}
