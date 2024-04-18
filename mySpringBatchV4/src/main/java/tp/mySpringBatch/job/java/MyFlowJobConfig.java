package tp.mySpringBatch.job.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tp.mySpringBatch.decider.MySkipCheckingDecider;
import tp.mySpringBatch.listener.JobCompletionNotificationListener;
import tp.mySpringBatch.tasklet.PrintMessageWithDelayTasklet;

@Configuration
@Profile("!xmlJobConfig")
public class MyFlowJobConfig extends MyAbstractJobConfig{

  public static final Logger logger = LoggerFactory.getLogger(MyFlowJobConfig.class);


  @Bean(name="mySimpleSequentialStepsJob")
  public Job mySimpleSequentialStepsJob() {
    var name = "mySimpleSequentialStepsJob";
    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
    
    Step step1 = stepBuilder.transactionManager(this.batchTxManager).tasklet(new PrintMessageWithDelayTasklet("from_step_1",2000L)).build();
    Step step2 = stepBuilder.transactionManager(this.batchTxManager).tasklet(new PrintMessageWithDelayTasklet("from_step_2",2000L)).build();
    Step step3 = stepBuilder.transactionManager(this.batchTxManager).tasklet(new PrintMessageWithDelayTasklet("from_step_3",2000L)).build();
    
    var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
    return jobBuilder
    		 .start(step1)
    		 .next(step2)
    		 .next(step3)
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }
  
  @Bean(name="mySimpleConditionalStepsJob")
  public Job mySimpleConditionalStepsJob() {
    var name = "mySimpleConditionalStepsJob";
    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
    
    Step step1 = stepBuilder.transactionManager(this.batchTxManager).tasklet(new PrintMessageWithDelayTasklet("from_step_1_ok",2000L)).build();
    //Step step1 = stepBuilder.transactionManager(this.batchTxManager).tasklet(new PrintMessageWithDelayTasklet("from_step_1_error",2000L)).build();
    Step step2 = stepBuilder.transactionManager(this.batchTxManager).tasklet(new PrintMessageWithDelayTasklet("from_step_2",2000L)).build();
    Step step3 = stepBuilder.transactionManager(this.batchTxManager).tasklet(new PrintMessageWithDelayTasklet("from_step_3",2000L)).build();
    
    var jobBuilder =  (new JobBuilder(name)).repository(jobRepository);
    return jobBuilder
    		 //.start(step1).on("FAILED").to(step3)
    		 .start(step1).on("FAILED").to(step3)
    		 .from(step1).on("*").to(step2)
    		 .from(step2).on("*").to(step3)
    		 .end()
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }
  
  @Bean
  public MySkipCheckingDecider mySkipCheckingDecider() {
	  return new MySkipCheckingDecider();
  }
  
  @Bean(name="withDecisionFlowJob")
  public Job withDecisionFlowJob(
		  MySkipCheckingDecider mySkipCheckingDecider,
		  @Qualifier("csvWithSkipsErrorsToJson") Step step1
		  // @Qualifier("csvToJson") Step step1
		  ) {
    var name = "withDecisionFlowJob";
    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
    
    Step stepWithSkips = stepBuilder.transactionManager(this.batchTxManager).tasklet(new PrintMessageWithDelayTasklet("COMPLETED_WITH_SKIPS",500L)).build();
    Step stepWithoutSkip = stepBuilder.transactionManager(this.batchTxManager).tasklet(new PrintMessageWithDelayTasklet("COMPLETED",500L)).build();
    
    var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
    
 
    final Flow withOrWithoutSkipFlow = new FlowBuilder<SimpleFlow>("withOrWithoutSkipFlow")
    		.start(step1)
    	    .next(mySkipCheckingDecider).on("COMPLETED_WITH_SKIPS").to(stepWithSkips)
            .from(mySkipCheckingDecider).on("COMPLETED").to(stepWithoutSkip)
            .end();//end of flow
    
    return jobBuilder
    		.start(withOrWithoutSkipFlow)
    		.end()//end FlowBuilder and return to JobBuilder 
    		.listener(new JobCompletionNotificationListener())
    		.build();
  }
  
  /*
  Autres possibilités (variantes):
  from_or_next(stepXy).on("FAILED").end()  to ending full job with COMPLETED status when intermediate stepXy "FAILED"
  from_or_next(stepXy).on("FAILED").fail() to ending full job with FAILED status when intermediate stepXy "FAILED"
  */
}