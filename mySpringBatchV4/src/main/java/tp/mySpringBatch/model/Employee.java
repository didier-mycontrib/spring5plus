package tp.mySpringBatch.model;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@XmlRootElement(name = "employee") //just for read/generate XML file wkith jaxb2 marshaller
public class Employee extends Person {
	private String function;
	private Double salary;
	
	@Override
	public String toString() {
		return "Employee [function=" + function + ", salary=" + salary + ", toString()=" + super.toString() + "]";
	}

	public Employee() {
		super();
	}

	public Employee(Long id, String firstName, String lastName, Integer age, Boolean active) {
		super(id, firstName, lastName, age, active);
	}

	public Employee(String firstName, String lastName, Integer age, Boolean active) {
		super(firstName, lastName, age, active);
	}
	
	public Employee(String firstName, String lastName, Integer age, Boolean active,String function,Double salary) {
		super(firstName, lastName, age, active);
		this.function=function;
		this.salary=salary;
	}
	
	public Employee(Long id, String firstName, String lastName, Integer age, Boolean active,String function,Double salary) {
		super(id, firstName, lastName, age, active);
		this.function=function;
		this.salary=salary;
	}
	
	
}
