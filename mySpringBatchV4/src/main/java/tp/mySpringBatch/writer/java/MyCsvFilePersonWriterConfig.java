package tp.mySpringBatch.writer.java;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;

import tp.mySpringBatch.model.EmployeeStat;
import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")
public class MyCsvFilePersonWriterConfig {
	  
	  @Value("file:data/output/csv/outputData.csv") //to read in project root directory
	  private WritableResource outputCsvResource;
	  
	  @Value("file:data/output/csv/employeeStats.csv") //to read in project root directory
	  private WritableResource empStatsCsvResource;
	  
	  
	  //V2 with builder:
	  @Bean @Qualifier("csv")
	  FlatFileItemWriter<Person> csvFilePersonWriter() {
		  
		  return new FlatFileItemWriterBuilder<Person>()
				  .name("csvFilePersonWriter")
				  .resource(outputCsvResource)
				  .delimited()
				  .delimiter(";")
				  .names("id","firstName", "lastName", "age", "active")
				  .headerCallback((writer)-> {writer.write("id;firstname;lastname;age;active");})
				  .build();
	  }
	  
	
	  
	  @Bean @Qualifier("csv")
	  FlatFileItemWriter<EmployeeStat> csvFileEmployeStatWriter() {
		  
		  return new FlatFileItemWriterBuilder<EmployeeStat>()
				  .name("csvFileEmployeStatWriter")
				  .resource(empStatsCsvResource)
				  .delimited()
				  .delimiter(";")
				  .names("function","numberOfEmployees", "minSalary", "maxSalary", "averageSalary")
				  .headerCallback((writer)-> {writer.write("function;numberOfEmployees;minSalary;maxSalary;averageSalary");})
				  .build();
	  }
	  
	 
	  /*
	  //V1 without builder and with sub methods:
	  
	  @Bean @Qualifier("csv")
	  FlatFileItemWriter<Person> personCsvFileItemWriter() {

		  //Create writer instance
		  FlatFileItemWriter<Person> writer = new FlatFileItemWriter<>();

		  //Set output file location
		  //WritableResource outputCsvResource = new FileSystemResource("data/output/csv/outputData.csv");
		  writer.setResource(outputCsvResource);

		  //All job repetitions should "append" to same output file
		  //writer.setAppendAllowed(true);
		  writer.setAppendAllowed(false);

		  writer.setLineAggregator(this.personLineAggregator(this.personFieldExtractor()));
		  return writer;
		}
	  
	  FieldExtractor<Person> personFieldExtractor(){
		  BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
		  beanWrapperFieldExtractor.setNames(new String[]{"id","firstName", "lastName", "age", "active"});
	      return beanWrapperFieldExtractor;
	  }
	  
	  LineAggregator<Person> personLineAggregator(FieldExtractor<Person> personFieldExtractor){
		  DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
		  delimitedLineAggregator.setDelimiter(";");
		  delimitedLineAggregator.setFieldExtractor(personFieldExtractor);
	      return delimitedLineAggregator;
	  }
	  */

}
