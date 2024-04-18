package tp.mySpringBatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

//ok as it without @Component and used with this code:
//.listener(new JobCompletionNotificationListener()) in a job definition @Bean
public class JobCompletionNotificationListener implements JobExecutionListener {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  @Override
  public void afterJob(JobExecution jobExecution) { 
    switch(jobExecution.getStatus()) {
    	case COMPLETED:
    		log.info(" --- JOB FINISHED (COMPLETED) : " + jobExecution.toString());
    	 	break;
    	case FAILED:
    		log.error(" xxx JOB FAILED : " + jobExecution.toString());
    		break;
    	default:
    		log.info(" --- JOB Terminated  : " + jobExecution.toString());
    }
  }

@Override
public void beforeJob(JobExecution jobExecution) {

}
  
  
}