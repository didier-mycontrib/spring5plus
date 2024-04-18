package tp.mySpringBatch.reader.custom;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import tp.mySpringBatch.model.Person;

public class PersonGeneratorReader extends AbstractPersonGenerator implements ItemReader<Person>{
	

	public PersonGeneratorReader() {
		super();
	}

	public PersonGeneratorReader(long dataSetSize) {
		super(dataSetSize);
	}

	private Person generatePerson(){
		index++;
		int nbFirstNames = firstNameList.size();
		int nbLastNames = lastNameList.size();
		if(index<=dataSetSize) {
			double randomCoeff = Math.random();
			int age = (int)((100 * randomCoeff) % 100);
			String firstName = firstNameList.get((int)(nbFirstNames * randomCoeff) % nbFirstNames);
			String lastName = lastNameList.get((int)(nbLastNames * randomCoeff) % nbLastNames);
			return new Person(firstName , lastName ,age  , true );
		}else
			return null;
	}
		
	@Override
	public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return generatePerson();
	}


}
