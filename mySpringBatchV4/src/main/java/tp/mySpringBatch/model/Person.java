package tp.mySpringBatch.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "person") //just for read/generate XML file wkith jaxb2 marshaller
//implements Serializable just for serialize the execution context (if needed)
public class Person implements Serializable{
	private Long id; //auto-incremented in database (may be null before insert)
	private String firstName;
	private String lastName;
	private Integer age;
	private Boolean active;
    
	public Person(String firstName, String lastName, Integer age, Boolean active) {
		super();
		//this.id may be  null 
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.active = active;
	}
    
    
}