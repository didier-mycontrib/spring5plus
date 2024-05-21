package tp.tpSpringBatch.job;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import tp.tpSpringBatch.config.JobRepositoryBatchConfig;
import tp.tpSpringBatch.job.java.HelloWorldJobConfig;
import tp.tpSpringBatch.tasklet.bean.PrintHelloWorldMessageTaskletBean;


@Configuration
@Import({JobRepositoryBatchConfig.class,
	    HelloWorldJobConfig.class ,
		PrintHelloWorldMessageTaskletBean.class})
class HelloWorldJobTestConfig{
	
}

@SpringBatchTest
@SpringJUnitConfig({ HelloWorldJobTestConfig.class } )
@ActiveProfiles(profiles = {})
public class TestHelloWorldJob {
	
	Logger logger = LoggerFactory.getLogger(TestHelloWorldJob.class);
	
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
		
	@Autowired 
	//no need of @Qualifier("myHelloWorldJob") because only one unique job should be found
	//in @SpringBatchTest configuration (good pratice in V5 , mandatory in SprinBatch V4)
	private Job job;
	
	@Test
	public void testHelloWorldJob() throws Exception {
		 this.jobLauncherTestUtils.setJob(job);
	     JobExecution jobExecution = jobLauncherTestUtils.launchJob();
	     logger.debug("jobExecution="+jobExecution.toString());
	     assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
	}

}
