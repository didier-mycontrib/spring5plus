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
import tp.mySpringBatch.model.EmployeeStat;
import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.processor.IncreaseEmployeeSalaryProcessor;

@Configuration
@Profile("!xmlJobConfig")
public class IncreaseEmployeeSalaryJobConfig extends MyAbstractJobConfig{

  public static final Logger logger = LoggerFactory.getLogger(IncreaseEmployeeSalaryJobConfig.class);
 

  @Bean(name="increaseEmployeeSalaryJob")
  public Job increaseEmployeeSalaryJob(
		  @Qualifier("increaseSalaryInDb") Step step1,
		  @Qualifier("extractEmployeeStatFromDbToCsv") Step step2
		  ) {
    var name = "increaseEmployeeSalaryJob";
    var builder = (new JobBuilder(name)).repository(jobRepository);
    return builder
    		.start(step1)
    		.next( step2)
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }

  
  @Bean @Qualifier("increaseSalaryInDb")
  public Step increaseSalaryInDb(
		                    @Qualifier("db") ItemReader<Employee> reader,
		                    @Qualifier("update_db") ItemWriter<Employee> writer,
		                    IncreaseEmployeeSalaryProcessor increaseEmployeeSalaryProcessor) {
    var name = "increaseSalaryInDb";
    var builder = (new StepBuilder(name)).repository(jobRepository);
    return builder
    	.transactionManager(batchTxManager)
    	.<Employee, Employee>chunk(10)
        .reader(reader)
        .processor(increaseEmployeeSalaryProcessor)
        .writer(writer)
        .build();
  }
  
  @Bean @Qualifier("extractEmployeeStatFromDbToCsv")
  public Step extractEmployeeStatFromDbToCsv(
		                    @Qualifier("db") ItemReader<EmployeeStat> reader,
		                    @Qualifier("csv") ItemWriter<EmployeeStat> writer) {
    var name = "extractEmployeeStatFromDbToCsv";
    var builder =(new StepBuilder(name)).repository(jobRepository);
    return builder
    	.transactionManager(batchTxManager)
        .<EmployeeStat, EmployeeStat>chunk(1)
        .reader(reader)
        .writer(writer)
        .build();
  }
 


}