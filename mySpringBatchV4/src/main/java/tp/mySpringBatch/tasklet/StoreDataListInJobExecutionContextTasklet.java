package tp.mySpringBatch.tasklet;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class StoreDataListInJobExecutionContextTasklet<T> implements Tasklet , StepExecutionListener{
	
	private List<T> dataList;
	
	public static final Logger logger = LoggerFactory.getLogger(StoreDataListInJobExecutionContextTasklet.class);
	 
	public StoreDataListInJobExecutionContextTasklet(List<T> dataList){
		this.dataList=dataList;
	}
	
	public StoreDataListInJobExecutionContextTasklet(){
		this(new ArrayList<T>());//empty list by default
	}
	
	
	@Override
    public void beforeStep(StepExecution stepExecution) {
		//dataList = new ArrayList<>();
		//logger.debug("GeneratePersonsTasklet.beforeStep() was called");
    }
   
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		//contribution.getStepExecution().getJobExecution().getExecutionContext().put("...", "...");
		//now or later in afterStep
		
		/*
		this.dataList.add(new Person("Paul","Ochon", 34, true));
		this.dataList.add(new Person("Laurent","Houtan", 29, false));
		this.dataList.add(new Person("Alain","Verse", 42, false));
		*/
		
		logger.debug("GeneratePersonsTasklet.execute() was called with dataList="+dataList);
		
		return RepeatStatus.FINISHED;
	}
	
	@Override
    public ExitStatus afterStep(StepExecution stepExecution) {
		stepExecution
        .getJobExecution()
        .getExecutionContext()
        .put("dataList", this.dataList);
		logger.debug(">>>>> GeneratePersonsTasklet.afterStep() was called with this.dataList="+this.dataList);
		return ExitStatus.COMPLETED;
	}


}
