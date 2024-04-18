package tp.mySpringBatch.job.java;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;

import tp.mySpringBatch.listener.JobCompletionNotificationListener;

public abstract class MyAbstractJobConfig {
	
	@Autowired
	protected JobRepository jobRepository;
	
	@Autowired
	protected PlatformTransactionManager batchTxManager;
	
	//NB:  jobRepository will be useful in Job concrete SubClass to build new Job and new Steps
	//    batchTxManager will be useful in Job concrete SubClass to build new Steps
	
	protected Job buildMySingleStepJob(String jobName, Step singleStep) {
	    var jobBuilder = (new JobBuilder(jobName)).repository(jobRepository);
	    return jobBuilder.start(singleStep)
	    		.listener(new JobCompletionNotificationListener())
	    		.build();
	  }
	
}
