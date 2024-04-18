package tp.mySpringBatch.job.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;

import tp.mySpringBatch.listener.JobCompletionNotificationListener;
import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.partitioner.MyRangePartitioner;

@Configuration
@Profile("!xmlJobConfig")
public class MyPartitionJobConfig extends MyAbstractJobConfig{

  public static final Logger logger = LoggerFactory.getLogger(MyPartitionJobConfig.class);
  
  @Bean(name="myPartitionJob")
  public Job myPartitionJob(@Qualifier("managerDbToCsv") Step managerStep) {
    var name = "myPartitionJob";
    var builder = (new JobBuilder(name)).repository(jobRepository);
    return builder.start(managerStep).listener(new JobCompletionNotificationListener()).build();
  }


  @Bean
  public Partitioner myPartitioner() {
	  return new MyRangePartitioner(5);//rangeSize=5
  }
  
  @Bean
  public TaskExecutor myTaskExecutor() {
	  return new org.springframework.core.task.SimpleAsyncTaskExecutor();
  }

  @Bean @Qualifier("managerDbToCsv")
  public Step managerStep(
		  @Qualifier("myTaskExecutor")TaskExecutor taskExecutor,
		  @Qualifier("myPartitioner") Partitioner partitioner,
		  @Qualifier("workerDbToCsv") Step workerStep) {
	  var name = "managerStep";
	  var builder = (new StepBuilder(name)).repository(jobRepository);
      return builder
          .partitioner("workerStep", partitioner)
          .step(workerStep)
          .gridSize(4)
          .taskExecutor(taskExecutor)
          .build();
  }
  
  @Bean @Qualifier("workerDbToCsv")
  public Step workStepDbToCsv(@Qualifier("db_withPartition") ItemReader<Person> reader,
		                      @Qualifier("csv_withPartition") ItemWriter<Person> writer) {
    var name = "workStepDbToCsv";
    var builder = (new StepBuilder(name)).repository(jobRepository);
    return builder
    	.transactionManager(batchTxManager)
        .<Person, Person>chunk(1)
        .reader(reader)
        .writer(writer)
        .build();
  }
  

}