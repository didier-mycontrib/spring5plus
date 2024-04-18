package tp.mySpringBatch.model;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@XmlRootElement(name = "personWithNumAndAddress")
public class PersonWithNumAndAddress extends Person{
	
	private SocialSecurityNumber socialSecurityNumber; 
	private Address address;
	
	public PersonWithNumAndAddress(){
		super();
		socialSecurityNumber = new SocialSecurityNumber();
		address = new Address();
	}
	
	public PersonWithNumAndAddress(String firstName, String lastName, Integer age, Boolean active,
			String socialSecurityNumberAsString,
			/*Address*/ String addressAsString){
		super(firstName,lastName,age,active);
		//this.socialSecurityNumber=socialSecurityNumberAsString;
		this.socialSecurityNumber = new SocialSecurityNumber(socialSecurityNumberAsString);
		this.address = new Address(	addressAsString);
	}
	
	public PersonWithNumAndAddress(Long id, String firstName, String lastName, Integer age, Boolean active,
			String socialSecurityNumberAsString,
			/*Address*/ String addressAsString){
		super(id,firstName,lastName,age,active);
		//this.socialSecurityNumber=socialSecurityNumberAsString;
		this.socialSecurityNumber = new SocialSecurityNumber(socialSecurityNumberAsString);
		this.address = new Address(	addressAsString);
	}

	@Override
	public String toString() {
		return "PersonWithAddress [socialSecurityNumber =" + socialSecurityNumber + ", address=" + address + ", toString()=" + super.toString() + "]";
	}
}
