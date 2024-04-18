package tp.mySpringBatch.reader.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.model.PersonWithNumAndAddress;
import tp.mySpringBatch.reader.mapper.PersonWithNumAndAddressLineMapper;

@Configuration
@Profile("!xmlJobConfig")
public class MyCsvFilePersonReaderConfig { 
	
	public static final Logger logger = LoggerFactory.getLogger(MyCsvFilePersonReaderConfig.class);
	
	@Value("file:data/input/csv/inputData.csv") //to read in project root directory
	  //NB: by default @Value(path) is @Value("classpath:path) //to read in src/main/resource or other classpath part
	  private Resource inputCsvResource;
	
	@Value("file:data/input/csv/personWithNumAndAddress.csv") //to read in project root directory
	  private Resource inputCsvWithNumAndAddressResource;
	
	@Value("file:data/input/csv/inputDataWithErrors.csv") //to read in project root directory
	  private Resource inputCsvWithErrorsResource;

	
	//V2 with FlatFileItemReaderBuilder
	  @Bean @Qualifier("csv")
	  @JobScope
	  public FlatFileItemReader<Person> personCsvFileReader(
				@Value("#{jobParameters['msg1']}") String msg1
			  ) {
		  
		//just to see that @Value("#{jobParameters['msg1']}")
		//can be used in @JobScope or @StepScope ItemReader @Bean
		logger.info("in @Bean personCsvFileReader() , msg1="+msg1);
		
		
		return new FlatFileItemReaderBuilder<Person>()
				.name("personCsvFileReader")
				.resource(inputCsvResource)
				.linesToSkip(1)
				.delimited()
				.delimiter(";")
				.names("id","firstName", "lastName", "age", "active")
				.targetType(Person.class)
				.build();
	  }
	  
	  @Bean @Qualifier("csv")
	  @JobScope
	  public FlatFileItemReader<PersonWithNumAndAddress> personCsvWithNumAndAddressFileReader() {
		  
		var specificComplexLineMapper = new PersonWithNumAndAddressLineMapper();
		
		return new FlatFileItemReaderBuilder<PersonWithNumAndAddress>()
				.name("personCsvWithNumAndAddressFileReader")
				.resource(inputCsvWithNumAndAddressResource)
				.linesToSkip(1)
				.lineMapper(specificComplexLineMapper)
				.build();
	  }
	 
	  
	  @Bean @Qualifier("csvWithErrors")
	  @JobScope
	  public FlatFileItemReader<Person> personCsvWithErrorsFileReader() {
		 
		  
		return new FlatFileItemReaderBuilder<Person>()
				.name("personCsvWithErrorsFileReader")
				.resource(inputCsvWithErrorsResource)
				.linesToSkip(1)
				.lineMapper(null)
				.delimited()
				.delimiter(";")
				.names("id","firstName", "lastName", "age", "active")
				.targetType(Person.class)
				.build();
	  }
   

	/*
	  //OLD V1 with sub-methods and without builder :
	  
	    @Bean @Qualifier("csv")
	   public FlatFileItemReader<Person> personCsvFileReader() {
	    var personCsvFileItemReader = new FlatFileItemReader<Person>();
	    
	    
	    personCsvFileItemReader.setLineMapper(
	    		this.personLineMapper(this.personLineTokenizer(),
	    				              this.personFieldSetMapper()));
	    
	    //Resource inputCsvResource = new FileSystemResource("data/input/csv/inputData.csv");
	    personCsvFileItemReader.setResource(inputCsvResource);
	    
	    personCsvFileItemReader.setLinesToSkip(1);
	    
	    return personCsvFileItemReader;
	  }

	   
	  public DefaultLineMapper<Person> personLineMapper(LineTokenizer personLineTokenizer,
	                                              FieldSetMapper<Person> personFieldSetMapper) {
	    var lineMapper = new DefaultLineMapper<Person>();
	    lineMapper.setLineTokenizer(personLineTokenizer);
	    lineMapper.setFieldSetMapper(personFieldSetMapper);
	    return lineMapper;
	  }

	  public BeanWrapperFieldSetMapper<Person> personFieldSetMapper() {
	    var fieldSetMapper = new BeanWrapperFieldSetMapper<Person>();
	    fieldSetMapper.setTargetType(Person.class);
	    return fieldSetMapper;
	  }

	  public DelimitedLineTokenizer personLineTokenizer() {
	    var tokenizer = new DelimitedLineTokenizer();
	    tokenizer.setDelimiter(";");
	    tokenizer.setNames("id","firstName", "lastName", "age", "active");
	    return tokenizer;
	  }
      */
}

/*
 NB: https://docs.spring.io/spring-batch/reference/readers-and-writers/flat-files/file-item-reader.html
 possibilité de format mixte/hybride : PatternMatchingCompositeLineMapper lineMapper =
		new PatternMatchingCompositeLineMapper();
		...
 */
