package tp.mySpringBatch.job;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


public abstract class AbstractBasicTestJob {
	
	protected Logger logger = LoggerFactory.getLogger(AbstractBasicTestJob.class);
	
	protected String[] jobNames;//jobName to test , set by constructor of subclass
	
	@Autowired
	protected ApplicationContext applicationContext;
	
	@Autowired
	protected JobLauncherTestUtils jobLauncherTestUtils;
	
	@Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
  
    @AfterEach
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

	
	//to override in subClasses
	public JobParameters initJobParameters() {
		return new JobParametersBuilder()
				 .addLong("timeStampOfJobInstance", System.currentTimeMillis())//Necessary for running several instances of a same job (each jobInstance must have a parameter that changes)
				//.addString("paramName", "paramValue")
				.toJobParameters();
	}
		
	
	public AbstractBasicTestJob(String...jobNames) {
		this.jobNames=jobNames;
	}
	
	
	public void prepareJobFromJobName(String jobName) {
		
		Job job = (Job) applicationContext.getBean(jobName);
		logger.debug("TESTING "+jobName);
		this.jobLauncherTestUtils.setJob(job);
	}
		
	@Test
	public void basicGenericTestJob() throws Exception {
		JobParameters jobParameters = initJobParameters();
		logger.debug(">>>> jobNames=" + Arrays.asList(this.jobNames));
		for(String jobName :  jobNames) {
			prepareJobFromJobName(jobName);
			JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
		    logger.debug("jobExecution="+jobExecution.toString());
		    assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
		} 
	}

}
