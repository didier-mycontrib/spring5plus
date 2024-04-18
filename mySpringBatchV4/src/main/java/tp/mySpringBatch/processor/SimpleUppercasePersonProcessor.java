package tp.mySpringBatch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tp.mySpringBatch.model.Person;

@Component
@StepScope
//@JobScope
public class SimpleUppercasePersonProcessor implements ItemProcessor<Person,Person>{
	
	public static final Logger logger = LoggerFactory.getLogger(SimpleUppercasePersonProcessor.class);
	
	@Value("#{jobParameters['enableUpperCase']}") 
	private Boolean enableUpperCase=true;

	@Override
	public Person process(Person pers) throws Exception {
		String lastName = enableUpperCase?pers.getLastName().toUpperCase():pers.getLastName();
		Person person=new Person(pers.getId(),pers.getFirstName(),lastName,pers.getAge(),pers.getActive());
		logger.debug("PPPP  SimpleUppercasePersonProcessor.process() with enableUpperCase="+enableUpperCase + "returning person=" + person);
		return person;
	}
}
