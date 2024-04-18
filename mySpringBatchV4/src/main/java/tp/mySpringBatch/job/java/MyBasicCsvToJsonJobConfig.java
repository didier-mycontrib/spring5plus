package tp.mySpringBatch.job.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemReaderException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tp.mySpringBatch.exception.MyProcessException;
import tp.mySpringBatch.listener.JobCompletionNotificationListener;
import tp.mySpringBatch.listener.MySkippedErrorsListener;
import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.model.PersonWithNumAndAddress;
import tp.mySpringBatch.processor.SimpleUppercasePersonProcessor;
import tp.mySpringBatch.processor.UppercasePersonProcessorWithFailuresForRetry;

@Configuration
@Profile("!xmlJobConfig")
public class MyBasicCsvToJsonJobConfig extends MyAbstractJobConfig{

  public static final Logger logger = LoggerFactory.getLogger(MyBasicCsvToJsonJobConfig.class);


  @Bean
  public Job fromCsvToJsonJob(@Qualifier("csvToJson") Step step1) {
    var name = "fromCsvToJsonJob";//same as method name for simple mind developper
    var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
    return jobBuilder.start(step1)
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }

  @Bean @Qualifier("csvToJson")
  public Step stepCsvToJson(@Qualifier("csv") ItemReader<Person> personItemReader,
		            @Qualifier("json") ItemWriter<Person> personItemWriter ,
		            SimpleUppercasePersonProcessor simpleUppercasePersonProcessor) {
    var name = "stepCsvToJson";//same as method name for simple mind developper
    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
    return stepBuilder
    	.transactionManager(batchTxManager)
        .<Person, Person>chunk(5)
        .reader(personItemReader)
        .processor(simpleUppercasePersonProcessor)
        .writer(personItemWriter)
        .build();
  }
  
  @Bean
  public Job fromCsvWithNumAndAddressToJsonJob(@Qualifier("csvWithNumAndAddressToJson") Step step1) {
    var name = "fromCsvWithNumAndAddressToJsonJob";//same as method name for simple mind developper
    var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
    return jobBuilder.start(step1)
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }

  
  @Bean @Qualifier("csvWithNumAndAddressToJson")
  public Step stepCsvWithNumAndAddressToJson(@Qualifier("csv") ItemReader<PersonWithNumAndAddress> personItemReader,
		            @Qualifier("json") ItemWriter<PersonWithNumAndAddress> personItemWriter ) {
    var name = "stepCsvWithNumAndAddressToJson";//same as method name for simple mind developper
    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
    return stepBuilder
    	.transactionManager(batchTxManager)
        .<PersonWithNumAndAddress, PersonWithNumAndAddress>chunk(5)
        .reader(personItemReader)
        .writer(personItemWriter)
        .build();
  }
  
  @Bean
  public Job fromCsvWithSkipsErrorsToJsonJob(@Qualifier("csvWithSkipsErrorsToJson") Step step1) {
    var name = "fromCsvWithSkipsErrorsToJsonJob"; //same as method name for simple mind developper
    var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
    return jobBuilder.start(step1)
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }

  @Bean @Qualifier("csvWithSkipsErrorsToJson")
  public Step stepCsvWithSkipsErrorsToJson(@Qualifier("csvWithErrors") ItemReader<Person> personItemReader,
		            @Qualifier("json") ItemWriter<Person> personItemWriter ,
		            SimpleUppercasePersonProcessor simpleUppercasePersonProcessor) {
    var name = "stepCsvWithSkipsErrorsToJson";
    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
    return stepBuilder
    	.transactionManager(batchTxManager)
        .<Person, Person>chunk(5)
        .listener(new MySkippedErrorsListener())
        .reader(personItemReader)
        .processor(simpleUppercasePersonProcessor)
        .writer(personItemWriter)
        .faultTolerant().skipLimit(5).skip(ItemReaderException.class) //IMPORTANT
        .listener(new MySkippedErrorsListener())//IMPORTANT
        .build();
    
    //NB: .skipLimit(globalInclusiveMaxLimit) , not consecutive error count but global count
  }
  
  @Bean
  public Job fromCsvToJsonWithRetryJob(@Qualifier("csvToJsonWithRetry") Step step1) {
    var name = "fromCsvToJsonWithRetryJob";
    var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
    return jobBuilder.start(step1)
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }

  @Bean @Qualifier("csvToJsonWithRetry")
  public Step stepCsvToJsonWithRetry(@Qualifier("csv") ItemReader<Person> personItemReader,
		            @Qualifier("json") ItemWriter<Person> personItemWriter ,
		            UppercasePersonProcessorWithFailuresForRetry uppercasePersonProcessorWithFailuresForRetry) {
    var name = "stepCsvToJsonWithRetry";
    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
    return stepBuilder
    	.transactionManager(batchTxManager)
        .<Person, Person>chunk(5)
        .listener(new MySkippedErrorsListener())
        .reader(personItemReader)
        .processor(uppercasePersonProcessorWithFailuresForRetry)
        .writer(personItemWriter)
        .faultTolerant()
        //.backOffPolicy(new ExponentialBackOffPolicy())
        .retryLimit(3).retry(MyProcessException.class) //IMPORTANT BUT BUG IN JAVA (not XML)  !!!!
        //MyProcessException a non Skippable exception !!!!
        .build();
    
  }
  
  

}