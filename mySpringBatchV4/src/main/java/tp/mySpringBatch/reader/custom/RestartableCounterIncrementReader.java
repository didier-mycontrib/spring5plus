package tp.mySpringBatch.reader.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class RestartableCounterIncrementReader implements ItemReader<Integer> , ItemStream{
	
	private Logger logger = LoggerFactory.getLogger(RestartableCounterIncrementReader.class);
	
	private Integer counter=0;

	@Override
	public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		counter++;
		logger.debug("#### RestartableCounterIncrementReader.read(): counter="+counter+ "*****");
		if(counter % 10 == 0)
			return null;
		else
		    return counter;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		this.counter = executionContext.getInt("counter", 0);
		logger.debug("#### RestartableCounterIncrementReader.open(): counter="+counter+ "*****");
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putInt("counter", counter);
		logger.debug("#### RestartableCounterIncrementReader.update(): counter="+counter+ "*****");
	}

	@Override
	public void close() throws ItemStreamException {
	}


}
