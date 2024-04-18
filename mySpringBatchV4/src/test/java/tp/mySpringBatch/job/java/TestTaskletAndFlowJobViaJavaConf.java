package tp.mySpringBatch.job.java;

import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;

import tp.mySpringBatch.MySpringBatchApplication;
import tp.mySpringBatch.job.AbstractBasicTestJob;

@SpringBatchTest
@SpringBootTest(classes = { MySpringBatchApplication.class} )
public class TestTaskletAndFlowJobViaJavaConf extends AbstractBasicTestJob {
	
	public TestTaskletAndFlowJobViaJavaConf() {
		//super("withMyTaskletJob","mySimpleSequentialStepsJob","mySimpleConditionalStepsJob"); //jobNames to test
		//super("mySimpleConditionalStepsJob");
		super("withDecisionFlowJob");
	}

}
