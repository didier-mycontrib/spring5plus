package tp.mySpringBatch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tp.mySpringBatch.model.Person;
import tp.mySpringBatch.partitioner.MyRangePartitioner;

@Component
@StepScope
public class WithDelayAndThreadNameLogPersonProcessor implements ItemProcessor<Person,Person>{
	
	private static Logger logger = LoggerFactory.getLogger(WithDelayAndThreadNameLogPersonProcessor.class);
	
    private Long delay=1000L; //1000ms par defaut
    
	public WithDelayAndThreadNameLogPersonProcessor(Long delay) {
		super();
		this.delay = delay;
	}
	
	public WithDelayAndThreadNameLogPersonProcessor() {
		super();
	}

	@Override
	public Person process(Person pers) throws Exception {
        Thread.sleep(delay);
        String message="person=" + pers + " processed by thread=" + Thread.currentThread().getName();
        logger.debug(message);
		return pers;
	}

	public Long getDelay() {
		return delay;
	}

	public void setDelay(Long delay) {
		this.delay = delay;
	}
	
}
