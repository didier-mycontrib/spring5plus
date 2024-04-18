package tp.mySpringBatch.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import tp.mySpringBatch.model.Person;

@Component
@StepScope
//@JobScope
public class FilterPersonByAgeProcessor implements ItemProcessor<Person,Person>{

	

	@Override
	public Person process(Person item) throws Exception {
		if (item.getAge()<18)
		   return null;
		else
		   return item;
	}

}
