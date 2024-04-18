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
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import tp.mySpringBatch.MySpringBatchApplication;
import tp.mySpringBatch.job.AbstractBasicTestJob;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest(classes = { MySpringBatchApplication.class} )
//@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class TestFileToFileJobViaJavaConf extends AbstractBasicTestJob {
	
	@Override
	public JobParameters initJobParameters() {
		return new JobParametersBuilder()
				.addLong("timeStampOfJobInstance", System.currentTimeMillis())//Necessary for running several instances of a same job (each jobInstance must have a parameter that changes)
				.addString("msg1", "_my_msg1_value_")//used by PrintJobParamMessageTaskletBean and some Reader/Writer
		        .addString("enableUpperCase", "true")//used by SimpleUppercasePersonProcessor
				.toJobParameters();
	}
	
	public TestFileToFileJobViaJavaConf() {
		super("copyFromCsvToCsvJob",
		"fromFixedPosTxtToCsvJob",
		"fromCsvToXmlJob",
		"fromCsvToJsonJob",
		"fromCsvToFixedPosTxtJob",
		"fromXmlToCsvJob",
		"fromJsonToXmlJob"); // jobNames to test
		//super(/*"fromCsvWithSkipsErrorsToJsonJob"*/);
		//super("fromCsvToJsonWithRetryJob"); //WITH BUG !!!!
	}
	
	@Test //test of a whole Job
	public void testCsvToJsonJob() throws Exception{
		
		//given 4 good lines in input file : data/input/csv/inputData.csv
		
		//when executing job "fromCsvToJsonJob"
		prepareJobFromJobName("fromCsvToJsonJob");
	    JobExecution jobExecution = jobLauncherTestUtils.launchJob(initJobParameters());
	    assertNotNull(jobExecution);
	    
	    logger.debug("jobExecution="+jobExecution.toString());
	    	    
	    ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
	    assertEquals("COMPLETED", actualJobExitStatus.getExitCode());
	    
	    //then actualResult file content should be the same as expected result file content
	    
	    FileSystemResource expectedResult = new FileSystemResource("data/expected_output/json/outputData.json");
	    FileSystemResource actualResult = new FileSystemResource("data/output/json/outputData.json");
	    //AssertFile.assertFileEquals(expectedResult, actualResult); //deprecated since v5
	    assertThat(actualResult.getFile()).hasSameTextualContentAs(expectedResult.getFile());//via AssertJ
	    
	}
	
	@Test //unit test of a single/individual Step 
	public void testStepCsvWithSkipsErrorsToJson() throws Exception{
		prepareJobFromJobName("fromCsvWithSkipsErrorsToJsonJob");//even if it is a unit test of an individual unit test , a job must be set in jobLauncherTestUtils
		JobExecution jobExecution = jobLauncherTestUtils.launchStep(
			      "stepCsvWithSkipsErrorsToJson", initJobParameters()); //excepted set name = full_name of the step integrated in the job
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		assertTrue(actualStepExecutions.size()==1);
		StepExecution stepExecution = actualStepExecutions.iterator().next();//first of collection
		long skipCount=stepExecution.getReadSkipCount();
		logger.debug("** skipCount="+skipCount );
		assertTrue(skipCount==5); //fichier data/input/inputDataWithErrors.csv with 5 errors
		
		long writeCount=stepExecution.getWriteCount();
		logger.debug("** writeCount="+skipCount );
		assertTrue(writeCount>=5);//fichier data/input/inputDataWithErrors.csv with 5 good lines
		                          //so data/output/outputData.csv must have 5 lines (+header)
		
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();
		assertEquals("COMPLETED", actualJobExitStatus.getExitCode());
		
	}
}
