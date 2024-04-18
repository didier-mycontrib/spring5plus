package tp.mySpringBatch.job.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tp.mySpringBatch.listener.JobCompletionNotificationListener;
import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")
public class CsvToDatabaseJobConfig extends MyAbstractJobConfig{

  public static final Logger logger = LoggerFactory.getLogger(CsvToDatabaseJobConfig.class);
 

  @Bean(name="insertIntoDbFromCsvJob")
  public Job insertIntoDbFromCsvJob(@Qualifier("csvToDb") Step step1) {
    var name = "insertIntoDbFromCsvJob";
    var builder = (new JobBuilder(name)).repository(jobRepository);
 
    return builder.start(step1).listener(new JobCompletionNotificationListener()).build();
  }

  @Bean @Qualifier("csvToDb")
  public Step stepCsvToDb(@Qualifier("csv") ItemReader<Person> reader,
		            @Qualifier("db") ItemWriter<Person> writer) {
    var name = "stepCsvToDb";
    var builder = (new StepBuilder(name)).repository(jobRepository);
    return builder
    	.transactionManager(batchTxManager)
        .<Person, Person>chunk(5)
        .reader(reader)
        .writer(writer)
        .build();
  }


}