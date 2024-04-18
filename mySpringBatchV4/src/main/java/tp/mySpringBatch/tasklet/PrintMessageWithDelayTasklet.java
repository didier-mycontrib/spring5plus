package tp.mySpringBatch.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

//no annotation //to use from xml config or in a annotated subclass in .bean subpackage
public class PrintMessageWithDelayTasklet implements Tasklet{
	
	private String message;
	
	private Long delay=1000L; //ms (1000=default value)
	
	
	
	
	public PrintMessageWithDelayTasklet(String message, Long delay) {
		super();
		this.message = message;
		this.delay = delay;
	}
	
	public PrintMessageWithDelayTasklet() {
		super();
	}


	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		Thread.sleep(delay);
		System.out.println(message);
		if(message==null ||  message.toLowerCase().contains("error"))
			contribution.setExitStatus(ExitStatus.FAILED);
		else
		    contribution.setExitStatus(ExitStatus.COMPLETED);
			//contribution.setExitStatus(new ExitStatus("OK"));
		//NB: if error detected in processor : this.stepExecution.setExitStatus(ExitStatus.FAILED);
		return RepeatStatus.FINISHED;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Long getDelay() {
		return delay;
	}


	public void setDelay(Long delay) {
		this.delay = delay;
	}
	
	

}
