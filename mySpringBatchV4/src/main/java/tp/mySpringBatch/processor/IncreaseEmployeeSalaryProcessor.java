package tp.mySpringBatch.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tp.mySpringBatch.model.Employee;

@Component
@StepScope
//@JobScope
public class IncreaseEmployeeSalaryProcessor implements ItemProcessor<Employee,Employee>{
	
	@Value("#{jobParameters['salaryIncreasePercentage']}") 
	private Double salaryIncreasePercentage=1.0;

	@Override
	public Employee process(Employee emp) throws Exception {
		Double newSalary = (emp.getSalary()!=null) ?  emp.getSalary()*(1+salaryIncreasePercentage/100) : null;
		return new Employee(emp.getId(),emp.getFirstName(), emp.getLastName(), emp.getAge() ,emp.getActive(),
				           emp.getFunction(),newSalary);
		
	}
}
