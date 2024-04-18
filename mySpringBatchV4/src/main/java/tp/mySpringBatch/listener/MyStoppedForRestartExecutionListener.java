package tp.mySpringBatch.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class MyStoppedForRestartExecutionListener implements StepExecutionListener {

	
	/*
	@Override
	public void beforeStep(StepExecution stepExecution) {
		StepExecutionListener.super.beforeStep(stepExecution);
	}
	*/

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		BatchStatus batchStatus = BatchStatus.STOPPED;
		//NO RESTART with default status of stepExecution = BatchStatus.COMPLETED 
		//RESTART with status = BatchStatus.FAILED or BatchStatus.STOPPED
		//IMPORTANT: job/step RESTART depends of BatchStatus (but not ExitStatus !!!) 
		stepExecution.setStatus(batchStatus);
		System.out.println("#### MyStoppedForRestartExecutionListener.afterStep() setting (batch)status="+batchStatus+ "*****");
		return ExitStatus.COMPLETED;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}
	
	
	/*
	 IMPORTANT , a job execution will be restart if all the following conditions are met:
	 	1) asking for restart via  jobOperator.restart(mostRecentJobExecution.getId());
	 	2) the status (BatchStatus , not ExitStatus) of a step is FAILED or STOPPED
	 ----
	 the status of the whole job will be automatically set to FAILED or STOPPED
	 if the status of a step is FAILED or STOPPED
	 */
	
  

}
