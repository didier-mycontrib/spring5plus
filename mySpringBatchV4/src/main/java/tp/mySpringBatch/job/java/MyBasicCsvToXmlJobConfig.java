package tp.mySpringBatch.job.java;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tp.mySpringBatch.listener.JobCompletionNotificationListener;
import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.processor.FilterPersonByAgeProcessor;
import tp.mySpringBatch.processor.SimpleUppercasePersonProcessor;

@Configuration
@Profile("!xmlJobConfig")
public class MyBasicCsvToXmlJobConfig extends MyAbstractJobConfig{ 

  public static final Logger logger = LoggerFactory.getLogger(MyBasicCsvToXmlJobConfig.class);


  @Bean
  public Job fromCsvToXmlJob(@Qualifier("csvToXml") Step step1) {
    var name = "fromCsvToXmlJob"; 
    var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
    return jobBuilder.start(step1)
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }
  
  @Bean @StepScope @Qualifier("upperCaseLastname_and_ageFiltering")
  public CompositeItemProcessor<Person,Person> myUppercaseLastnameAgeFilteringCompositeProcessor(
		  FilterPersonByAgeProcessor filterPersonByAgeProcessor,
          SimpleUppercasePersonProcessor simpleUppercasePersonProcessor 
		  ) {
      CompositeItemProcessor<Person,Person> processor = new CompositeItemProcessor<>();
      processor.setDelegates(Arrays.asList(filterPersonByAgeProcessor,
    		                               simpleUppercasePersonProcessor));
      return processor;
  }

  @Bean @Qualifier("csvToXml")
  public Step stepCsvToXml(@Qualifier("csv") ItemReader<Person> personItemReader,
		            @Qualifier("xml") ItemStreamWriter<Person> personItemWriter,
		            @Qualifier("upperCaseLastname_and_ageFiltering") CompositeItemProcessor compositeProcessor ) {
    var name = "stepCsvToXml";
    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
    return stepBuilder
    	.transactionManager(batchTxManager)
        .<Person, Person>chunk(5)
        .reader(personItemReader)
        .processor(compositeProcessor)
        .writer(personItemWriter)
        .build();
  }
  
  

}