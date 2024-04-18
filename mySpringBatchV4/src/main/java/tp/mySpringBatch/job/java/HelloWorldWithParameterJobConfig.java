package tp.mySpringBatch.job.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tp.mySpringBatch.listener.JobCompletionNotificationListener;
import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.tasklet.bean.PrintJobParamMessageTaskletBean;

@Configuration
@Profile("!xmlJobConfig")
public class HelloWorldWithParameterJobConfig extends MyAbstractJobConfig{

  //HelloWorldWithParameterJobConfig uses simplePrintMessageStep defined in HelloWorldJobConfig	

  public static final Logger logger = LoggerFactory.getLogger(HelloWorldWithParameterJobConfig.class);

  //ideas for jobParameters : sourceDir , destDir, ...
  
  /*
   NB: @Value("#{jobParameters['jobParameterName']}") 
   is only accessible from "execution_@Bean" that are declared in @JobScope or @StepScope
   execution_@Bean can be a Tasklet bean or ...
                   but cannot be a "job_configuration_bean"
   */
  		

  @Bean(name="myHelloWorldWithParameterJob")
  public Job myHelloWorldWithParameterJob(
		  @Qualifier("simplePrintMessageStep") Step printMessageStepWithTasklet ,
		  @Qualifier("simplePrintMessageFromJobParameterStep") Step printMessageFromJobParameterStep
		  ) {
    var name = "myHelloWorldWithParameterJob";
    var jobBuilder = (new JobBuilder(name)).repository(jobRepository);
    return jobBuilder
    		 .start(printMessageStepWithTasklet)
    		 .next(printMessageFromJobParameterStep)
    		.listener(new JobCompletionNotificationListener())
    		.build();
    
    //NB: printMessageFromJobParameterStep is based on printJobParamMessageTasklet
    //and tasklet.PrintJobParamMessageTasklet uses @Value("#{jobParameters['msg1']}")  
  }


 
  @Bean /* default @Qualifier("simplePrintMessageFromJobParameterStep") */
  //NB: @StepScope and @Value("#{jobParameters['msg1']}") String msg1
  //cannot be used here (ON A CONFIGURATION STEP BEAN)
  //but can be used on a tasklet bean
  public Step simplePrintMessageFromJobParameterStep(
		  PrintJobParamMessageTaskletBean printJobParamMessageTaskletBean
		  ){
	    var name = "simplePrintMessageFromJobParameterStep";
	    var stepBuilder = (new StepBuilder(name)).repository(jobRepository);
	    return stepBuilder
	    	.transactionManager(batchTxManager)
	        .tasklet(printJobParamMessageTaskletBean)
	        .build();
	  }


}