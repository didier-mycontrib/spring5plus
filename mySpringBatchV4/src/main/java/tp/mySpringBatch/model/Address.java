package tp.mySpringBatch.model;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "address") //just for read/generate XML file wkith jaxb2 marshaller
public class Address {
	private String countryCode;// (iso 3166-1 sur 2 ou 3 caractères) (ex: "FRA" , ...)
	private String zip ; 
	private String town;  
	private String street; 
	private String number; 
	private String complements; 
	
	public Address(String addressAsString) {
		String[] parts = addressAsString.split("!");
		this.countryCode = parts[0];
		this.zip = parts[1];
		this.town = parts[2];
		if(parts.length>=4)
		   this.street = parts[3];
		if(parts.length>=5)
		    this.number = parts[4];
		if(parts.length>=6)
		    this.complements = parts[5];
	}
}

//Exemple (with "!" delimiter"): FRA!75001!Paris!rue xy!2!1er etage
