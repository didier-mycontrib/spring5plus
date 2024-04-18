package tp.mySpringBatch.tasklet.bean;

import org.springframework.stereotype.Component;

import tp.mySpringBatch.tasklet.PrintMessageTasklet;

@Component
public class PrintHelloWorldMessageTaskletBean extends PrintMessageTasklet{
	public PrintHelloWorldMessageTaskletBean(){
		super("hello world by SpringBatch");
	}
}
