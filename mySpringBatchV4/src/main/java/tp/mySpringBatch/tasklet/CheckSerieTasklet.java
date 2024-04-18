package tp.mySpringBatch.tasklet;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

//no annotation //to use from xml config or in a annotated subclass in .bean subpackage
public class CheckSerieTasklet implements Tasklet{
	

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		System.out.println("**** CheckSerieTasklet: 5s de pause *****");
		Thread.sleep(1000); //5s de pause
		
		Properties serieProps = new Properties();
		Resource outputPersonSerieCsvResource = new FileSystemResource("data/input/series/persons.properties");
		InputStream is = outputPersonSerieCsvResource.getInputStream();
		serieProps.load(is);
		int lastReady = Integer.parseInt(serieProps.getProperty("lastReady","0"));
		int serieSize = Integer.parseInt(serieProps.getProperty("serieSize","0"));
		
		System.out.println("**** CheckSerieTasklet: lastReady="+lastReady +" serieSize=" + serieSize + " read from persons.properties and stored in job execution context **** after pause=5s ****");
		
		contribution.getStepExecution().getJobExecution().getExecutionContext().putInt("lastReady", lastReady);
		contribution.getStepExecution().getJobExecution().getExecutionContext().putInt("serieSize", serieSize);
		
		return RepeatStatus.FINISHED;
	}



	
	

}
