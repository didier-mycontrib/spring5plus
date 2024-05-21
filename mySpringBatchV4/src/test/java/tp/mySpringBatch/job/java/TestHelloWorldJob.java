package tp.mySpringBatch.job.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import tp.mySpringBatch.tasklet.bean.PrintHelloWorldMessageTaskletBean;


@Configuration
@EnableBatchProcessing //nécessaire avec SpringBoot2/SpringBatch4 (pas absolument nécessaire avec springBoot3/springBatch5)
@EnableAutoConfiguration //springBoot & spring-boot-starter-batch autoConfig (application.properties)
@Import({HelloWorldJobConfig.class ,
		PrintHelloWorldMessageTaskletBean.class})
class HelloWorldJobTestConfig{
	
}

@SpringBatchTest
@SpringBootTest(classes = { HelloWorldJobTestConfig.class } )
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
