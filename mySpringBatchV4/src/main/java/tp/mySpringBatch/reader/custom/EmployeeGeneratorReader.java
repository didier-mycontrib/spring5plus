package tp.mySpringBatch.reader.custom;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import tp.mySpringBatch.model.Employee;

public class EmployeeGeneratorReader extends AbstractPersonGenerator implements ItemReader<Employee>{
	

	public EmployeeGeneratorReader() {
		super();
	}

	public EmployeeGeneratorReader(long dataSetSize) {
		super(dataSetSize);
	}

	private Employee generateEmployee(){
		index++;
		int nbFirstNames = firstNameList.size();
		int nbLastNames = lastNameList.size();
		int nbFunctions = functionList.size();
		if(index<=dataSetSize) {
			double randomCoeff = Math.random();
			int age = (int)((100 * randomCoeff) % 100);
			double salary = Math.floor(2000.0 * (1 + randomCoeff));
			String firstName = firstNameList.get((int)(nbFirstNames * randomCoeff) % nbFirstNames);
			String lastName = lastNameList.get((int)(nbLastNames * randomCoeff) % nbLastNames);
			String function = functionList.get((int)(nbFunctions * randomCoeff) % nbFunctions);
			return new Employee(firstName , lastName ,age  , true , function,salary );
		}else
			return null;
	}
		
	@Override
	public Employee read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return generateEmployee();
	}


}
