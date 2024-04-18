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

import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")

public class MyCsvFilePersonWriterWithPartitionConfig {
	  
	  @Bean @StepScope @Qualifier("csv_withPartition")
      FlatFileItemWriter<Person> csvFilePartitionPersonWriter(
    		  @Value("#{stepExecutionContext[fromId]}") String fromId,
			  @Value("#{stepExecutionContext[toId]}") String toId) {
		  
		  WritableResource outputPartitionCsvResource = new FileSystemResource("data/output/csv/outputData_" + fromId + "-" + toId + ".csv");
		  
		  return new FlatFileItemWriterBuilder<Person>()
				  .name("csvFilePersonWriter")
				  .resource(outputPartitionCsvResource)
				  .delimited()
				  .delimiter(";")
				  .names("id","firstName", "lastName", "age", "active")
				  .headerCallback((writer)-> {writer.write("id;firstname;lastname;age;active");})
				  .build();
	  }
	  
}
