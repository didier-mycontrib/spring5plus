package tp.mySpringBatch.job.xml;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import tp.mySpringBatch.MySpringBatchApplication;
import tp.mySpringBatch.job.AbstractBasicTestJob;

@SpringBatchTest
@SpringBootTest(classes = { MySpringBatchApplication.class} )
@ActiveProfiles(profiles = {"xmlJobConfig"})
public class TestFileToFileJobViaXmlConf extends AbstractBasicTestJob {
	
	@Override
	public JobParameters initJobParameters() {
		return new JobParametersBuilder()
				.addLong("timeStampOfJobInstance", System.currentTimeMillis())//Necessary for running several instances of a same job (each jobInstance must have a parameter that changes)
				.addString("msg1", "_my_msg1_value_")//used by PrintJobParamMessageTaskletBean and some Reader/Writer
		        .addString("enableUpperCase", "true")//used by SimpleUppercasePersonProcessor
				.toJobParameters();
	}
	
	public TestFileToFileJobViaXmlConf() {
		super("copyFromCsvToCsvJob",
				"fromFixedPosTxtToCsvJob",
				"fromCsvToXmlJob",
				"fromCsvToJsonJob",
				"fromCsvToFixedPosTxtJob",
				"fromXmlToCsvJob",
				"fromJsonToXmlJob"); // jobNames to test
		
		//super("fromCsvWithSkipsErrorsToJsonJob");
		//super("fromCsvToJsonWithRetryJob");
		//super("copyFromCsvToCsvJob");
	}
}