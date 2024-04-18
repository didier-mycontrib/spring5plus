package tp.mySpringBatch.job.java;

import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;

import tp.mySpringBatch.MySpringBatchApplication;
import tp.mySpringBatch.job.AbstractBasicTestJob;

@SpringBatchTest
@SpringBootTest(classes = { MySpringBatchApplication.class} )
public class TestDatabaseJobViaJavaConf extends AbstractBasicTestJob {
	
	public TestDatabaseJobViaJavaConf() {
		super("insertIntoDbFromCsvJob","insertIntoCsvFromDbJob","myPartitionJob"); //jobNames to test
	}

}
