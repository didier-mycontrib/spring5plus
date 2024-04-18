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
import tp.mySpringBatch.model.Employee;
import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")
public class DataSetGeneratorJobConfig extends MyAbstractJobConfig{

  public static final Logger logger = LoggerFactory.getLogger(DataSetGeneratorJobConfig.class);
 

  @Bean(name="generateDbDataSetJob")
  public Job generateDbDataSetJob(@Qualifier("generatorToDb") Step step1) {
    var name = "generateDbDataSetJob";
    var builder = (new JobBuilder(name)).repository(jobRepository);
    return builder.start(step1).listener(new JobCompletionNotificationListener()).build();
  }

  /*
  //V1 (Person)
  @Bean @Qualifier("generatorToDb")
  public Step generatorToDb(@Qualifier("fromMyGeneratorPersonReader") ItemReader<Person> reader,
		                    @Qualifier("generate_db") ItemWriter<Person> writer) {
    var name = "generatorToDb";
    var builder = new StepBuilder(name, jobRepository);
    return builder
        .<Person, Person>chunk(10, batchTxManager)
        .reader(reader)
        .writer(writer)
        .build();
  }
  */
  
 
  //V2 (Employee extends Person with 2 related tables: Person and Functions)
  @Bean @Qualifier("generatorToDb")
  public Step generatorToDb(@Qualifier("fromMyGeneratorEmployeeReader") ItemReader<Employee> reader,
		                    @Qualifier("generate_db") ItemWriter<Employee> writer) {
    var name = "generatorToDb";
    var builder = (new StepBuilder(name)).repository(jobRepository);
    return builder
    	.transactionManager(batchTxManager)
        .<Employee, Employee>chunk(10)
        .reader(reader)
        .writer(writer)
        .build();
  }
 


}