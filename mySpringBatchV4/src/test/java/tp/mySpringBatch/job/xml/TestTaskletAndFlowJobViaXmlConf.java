package tp.mySpringBatch.job.xml;

import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import tp.mySpringBatch.MySpringBatchApplication;
import tp.mySpringBatch.job.AbstractBasicTestJob;

@SpringBatchTest
@SpringBootTest(classes = { MySpringBatchApplication.class} )
@ActiveProfiles(profiles = {"xmlJobConfig"})
public class TestTaskletAndFlowJobViaXmlConf extends AbstractBasicTestJob {
	
	public TestTaskletAndFlowJobViaXmlConf() {
		//super("withMyTaskletJob","mySimpleSequentialStepsJob","mySimpleConditionalStepsJob"); //jobNames to test
         //super("mySimpleConditionalStepsJob");
		super("withDecisionFlowJob");
	}

}
