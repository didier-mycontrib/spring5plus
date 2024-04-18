package tp.mySpringBatch.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import tp.mySpringBatch.exception.MyProcessException;
import tp.mySpringBatch.model.Person;

@Component
@StepScope
//@JobScope
public class UppercasePersonProcessorWithFailuresForRetry implements ItemProcessor<Person,Person>{
	
	public static int numberOfFailures=0;
	public static int maxRetry=3;
	

	@Override
	public Person process(Person pers) throws Exception {
		String lastName = pers.getLastName().toUpperCase();
		Person person=new Person(pers.getId(),pers.getFirstName(),lastName,pers.getAge(),pers.getActive());
		
		numberOfFailures++;
		if(numberOfFailures==maxRetry)
			numberOfFailures = 0;
		if(numberOfFailures>0) {
			throw new MyProcessException("processExceptionSimulation with numberOfFailures="+numberOfFailures);
		}else {
		   return person;
		}
	}
}
