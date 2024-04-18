package tp.mySpringBatch.tasklet.bean;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tp.mySpringBatch.tasklet.PrintMessageTasklet;

@Component
@StepScope
//@JobScope
public class PrintJobParamMessageTaskletBean extends PrintMessageTasklet{
	
	@Value("#{jobParameters['msg1']}") 
	public void setMessage(String message) {
		super.setMessage(message);
	}


}
