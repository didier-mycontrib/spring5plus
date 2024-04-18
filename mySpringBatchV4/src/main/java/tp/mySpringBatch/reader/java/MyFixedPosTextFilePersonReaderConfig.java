package tp.mySpringBatch.reader.java;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")
public class MyFixedPosTextFilePersonReaderConfig {
	
	@Value("file:data/input/txt/fixedPositionInputData.txt") //to read in project root directory
	  //NB: by default @Value(path) is @Value("classpath:path) //to read in src/main/resource or other classpath part
	  private Resource inputTxtResource;
	
	//V2 with FlatFileItemReaderBuilder
	  @Bean @Qualifier("fixedPosTxt")
	   public FlatFileItemReader<Person> personFixedPosTxtFileReader() {
		  
		return new FlatFileItemReaderBuilder<Person>()
				.name("personFixedPosTxtFileReader")
				.resource(inputTxtResource)
				.targetType(Person.class)
				.fixedLength()
				.columns(new Range(1, 8),new Range(9, 32), new Range(33, 56), new Range(57, 60), new Range(61, 66))
				.names("id","firstName", "lastName", "age", "active")
				.build();
	  }

	 /*
	  //OLD V1 with sub-methods and without builder :
	    @Bean @Qualifier("fixedPosTxt")
	   public FlatFileItemReader<Person> personFixedPosTxtFileReader() {
	    var personFixPosTxtFileItemReader = new FlatFileItemReader<Person>();
	    
	    
	    personFixPosTxtFileItemReader.setLineMapper(
	    		this.personLineMapper(this.personLineFixedLengthTokenizer(),
	    				              this.personFieldSetMapper()));
	    
	    personFixPosTxtFileItemReader.setResource(inputTxtResource);
	    
	    
	    return personFixPosTxtFileItemReader;
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
	  
	  public FixedLengthTokenizer personLineFixedLengthTokenizer() {
	    var tokenizer = new FixedLengthTokenizer();

		tokenizer.setNames("firstName", "lastName", "age", "active");
		tokenizer.setColumns(new Range(1, 8),new Range(9, 32), new Range(33, 56), new Range(57, 60), new Range(61, 66));
		return tokenizer;
	  }
*/
	  
   
}
