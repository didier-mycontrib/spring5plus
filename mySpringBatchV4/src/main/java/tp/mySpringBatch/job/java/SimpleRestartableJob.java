package tp.mySpringBatch.job.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tp.mySpringBatch.listener.JobCompletionNotificationListener;
import tp.mySpringBatch.listener.MyStoppedForRestartExecutionListener;
import tp.mySpringBatch.reader.custom.RestartableCounterIncrementReader;
import tp.mySpringBatch.tasklet.bean.PrintHelloWorldMessageTaskletBean;
import tp.mySpringBatch.writer.custom.SimpleCounterWriter;

@Configuration
@Profile("!xmlJobConfig")
public class SimpleRestartableJob extends MyAbstractJobConfig {
	public static final Logger logger = LoggerFactory.getLogger(SimpleRestartableJob.class);

	@Bean
	public Job simpleCounterRestartableJob(
			@Qualifier("basicPrintMessageStep") Step step1,
			@Qualifier("simpleCounterStep") Step step2
			) {
		var name = "simpleCounterRestartableJob";
		var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
		return jobBuilder
				.start(step1)
				.next(step2)
				.listener(new JobCompletionNotificationListener())
				.build();
	}

	@Bean
	public ItemReader<Integer> simpleIncrementReaderWithFail() {
		return new RestartableCounterIncrementReader();
	}

	@Bean
	public ItemWriter<Integer> simpleCounterWriterWithFail() {
		return new SimpleCounterWriter();
	}
	
	@Bean
	  public Step basicPrintMessageStep(PrintHelloWorldMessageTaskletBean printFixedHelloWorldMessageTasklet){
	    var name = "basicPrintMessageStep";
	    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
	    return stepBuilder
	    	.transactionManager(batchTxManager)
	        .tasklet(printFixedHelloWorldMessageTasklet)
	        .allowStartIfComplete(true)
	        .build();
	  }
	
	/*
	 NB: when a jobExecution is restarted , by default , only the "FAILED/STOPPED" step will be relaunch
	     COMPLETED step are not restarted by default.
	     WITH  .allowStartIfComplete(true) , a completed step will be retarted (if a another step FAILED/STOPPED) .
	 */

	@Bean
	@Qualifier("simpleCounterStep")
	public Step simpleCounterStep(
			@Qualifier("simpleIncrementReaderWithFail") ItemReader<Integer> reader,
			@Qualifier("simpleCounterWriterWithFail") ItemWriter<Integer> writer) {
		var name = "simpleCounterStep";
		var stepBuilder = new StepBuilder(name).repository(jobRepository);
		return ((SimpleStepBuilder<Integer, Integer>) stepBuilder
				.transactionManager(batchTxManager)
				.<Integer, Integer>chunk(5)
				.startLimit(3)//all starts (first attemp plus restarts)
)
				.reader(reader)
				.writer(writer)
				.listener(new MyStoppedForRestartExecutionListener())
				.build();
	}
	
	/*
	IF more than 3 starts (1 start plus 2 restarts):
	org.springframework.batch.core.StartLimitExceededException: 
	Maximum start limit exceeded for step: simpleCounterStepStartMax: 3
	 */
}