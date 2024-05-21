package tp.tpSpringBatch.tasklet.bean;

import org.springframework.stereotype.Component;

import tp.tpSpringBatch.tasklet.PrintMessageTasklet;

@Component
public class PrintHelloWorldMessageTaskletBean extends PrintMessageTasklet{
	public PrintHelloWorldMessageTaskletBean(){
		super("hello world by SpringBatch");
	}
}
