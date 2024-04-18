package tp.mySpringBatch.job.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;

import tp.mySpringBatch.MySpringBatchApplication;
import tp.mySpringBatch.job.AbstractBasicTestJob;

@SpringBatchTest
@SpringBootTest(classes = { MySpringBatchApplication.class} )
public class TestHelloWorldJobViaJavaConf extends AbstractBasicTestJob {


	@Override
	public JobParameters initJobParameters() {
		return new JobParametersBuilder()
				.addLong("timeStampOfJobInstance", System.currentTimeMillis())//Necessary for running several instances of a same job (each jobInstance must have a parameter that changes)
				.addString("msg1", "_my_msg1_value_")//used by PrintJobParamMessageTaskletBean and some Reader/Writer
		        .addString("enableUpperCase", "true")//used by SimpleUppercasePersonProcessor
				.toJobParameters();
	}
	

	public TestHelloWorldJobViaJavaConf() {
		super("myHelloWorldJob" , "myHelloWorldWithParameterJob"); //jobNames to test
		//super();//empty list of very basic automatic tests
	}
	
	@Test //test of a whole Job
	public void testHelloWorldJob() throws Exception{
		prepareJobFromJobName("myHelloWorldJob");
	    JobExecution jobExecution = jobLauncherTestUtils.launchJob(initJobParameters());
	    assertNotNull(jobExecution);
	    
	    logger.debug("jobExecution="+jobExecution.toString());
	    
	    JobInstance actualJobInstance = jobExecution.getJobInstance();
	    assertEquals("myHelloWorldJob", actualJobInstance.getJobName());
	    
	    ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
	    assertEquals("COMPLETED", actualJobExitStatus.getExitCode());
	    /*
	    FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
	    FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);
	    AssertFile.assertFileEquals(expectedResult, actualResult);
	    */
	}
	
	@Test //unit test of a single/individual Step 
	public void testStepCsvWithSkipsErrorsToJson() throws Exception{
		prepareJobFromJobName("myHelloWorldJob");
		JobExecution jobExecution = jobLauncherTestUtils.launchStep(
			      "simplePrintMessageStep", initJobParameters()); 
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		assertTrue(actualStepExecutions.size()==1);
		
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
		assertEquals("COMPLETED", actualJobExitStatus.getExitCode());
	}
	
	
	
	
 
}
