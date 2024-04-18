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
import tp.mySpringBatch.processor.SimpleUppercasePersonProcessor;

@Configuration
@Profile("!xmlJobConfig")
public class MyBasicJsonToXmlJobConfig extends MyAbstractJobConfig{

  public static final Logger logger = LoggerFactory.getLogger(MyBasicJsonToXmlJobConfig.class);


  @Bean
  public Job fromJsonToXmlJob(@Qualifier("jsonToXml") Step step1) {
    var name = "fromJsonToXmlJob";
    var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
    return jobBuilder.start(step1)
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }

  @Bean @Qualifier("jsonToXml")
  public Step stepJsonToXml(@Qualifier("json") ItemReader<Person> personItemReader,
		                   @Qualifier("xml") ItemWriter<Person> personItemWriter,
				            SimpleUppercasePersonProcessor simpleUppercasePersonProcessor) {
    var name = "stepJsonToXml";
    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
    return stepBuilder
    	.transactionManager(batchTxManager)
        .<Person, Person>chunk(5)
        .reader(personItemReader)
        .processor(simpleUppercasePersonProcessor)
        .writer(personItemWriter)
        .build();
  }
  
  

}