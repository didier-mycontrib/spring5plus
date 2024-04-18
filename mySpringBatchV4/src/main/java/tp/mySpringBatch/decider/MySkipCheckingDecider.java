package tp.mySpringBatch.decider;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class MySkipCheckingDecider implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		//stepExecution as "lastStepExecution"
		if(stepExecution==null) return new FlowExecutionStatus(ExitStatus.UNKNOWN.toString());
		if(!ExitStatus.FAILED.equals(stepExecution.getExitStatus()) && stepExecution.getSkipCount()>0)
		    return new FlowExecutionStatus("COMPLETED_WITH_SKIPS");
		else
			return new FlowExecutionStatus(stepExecution.getExitStatus().getExitCode().toString());
	}
}
