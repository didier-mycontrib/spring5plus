package tp.mySpringBatch.reader.java;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import tp.mySpringBatch.model.Person;

class FromJobExecutionContextReader<T> implements ItemReader<T>/* , StepExecutionListener*/ {
	
	 public static final Logger logger = LoggerFactory.getLogger(FromJobExecutionContextReader.class);
	  
	
	private List<T> dataList;
	
	@BeforeStep
	//@Override
	public void beforeStep(StepExecution stepExecution) {
		this.dataList = (List<T>) stepExecution.getJobExecution().getExecutionContext().get("dataList");
		logger.debug("*** FromJobExecutionContextReader.beforeStep was called with this.dataList="+this.dataList);
	}

	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		logger.debug("** FromJobExecutionContextReader.read was called with this.dataList="+this.dataList);
		if(dataList == null || dataList.isEmpty())
			return null;
		else
		    return dataList.remove(0);
	}
}