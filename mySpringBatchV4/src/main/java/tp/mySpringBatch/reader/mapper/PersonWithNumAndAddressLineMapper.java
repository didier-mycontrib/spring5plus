package tp.mySpringBatch.reader.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;

import tp.mySpringBatch.model.Address;
import tp.mySpringBatch.model.PersonWithNumAndAddress;
import tp.mySpringBatch.model.SocialSecurityNumber;

public class PersonWithNumAndAddressLineMapper implements LineMapper<PersonWithNumAndAddress> {

	 public static final Logger logger = LoggerFactory.getLogger(PersonWithNumAndAddressLineMapper.class);

	@Override
	public PersonWithNumAndAddress mapLine(String line, int lineNumber) throws Exception {

		var lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(";");
		lineTokenizer.setNames("id","firstName", "lastName", "age", "active" , "socialSecurityNumber" , "address");
		
		FieldSet mainFieldSet = lineTokenizer.tokenize(line);
		
		var socialSecurityNumberSubLineTokenizer = new FixedLengthTokenizer();
		socialSecurityNumberSubLineTokenizer.setNames("genre", "anneeNaissance", "numMoisNaissance",
				              "numDepartementNaissance" , "numCommuneNaissance" , "numOrdreNaissance" ,
				              "clef");
		socialSecurityNumberSubLineTokenizer.setColumns(new Range(1, 1),new Range(2, 3), new Range(4, 5),
				              new Range(6, 7), new Range(8, 10) , new Range(11, 13),
				              new Range(14, 15));
		
		BeanWrapperFieldSetMapper<SocialSecurityNumber> socialSecurityNumberFieldSetMapper = new BeanWrapperFieldSetMapper<SocialSecurityNumber>();
		socialSecurityNumberFieldSetMapper.setTargetType(SocialSecurityNumber.class);
		
		FieldSet socialSecurityNumberSubFieldSet = socialSecurityNumberSubLineTokenizer.tokenize(mainFieldSet.readString("socialSecurityNumber"));
		SocialSecurityNumber socialSecurityNumber = 
				socialSecurityNumberFieldSetMapper.mapFieldSet(socialSecurityNumberSubFieldSet);
		
		var addressSubLineTokenizer = new DelimitedLineTokenizer();
		addressSubLineTokenizer.setDelimiter("!");
		addressSubLineTokenizer.setNames("countryCode","zip", "town", "street", "number" , "complements" );
		
		BeanWrapperFieldSetMapper<Address> addressFieldSetMapper = new BeanWrapperFieldSetMapper<Address>();
		addressFieldSetMapper.setTargetType(Address.class);
		
		FieldSet addressSubFieldSet = addressSubLineTokenizer.tokenize(mainFieldSet.readString("address"));
		Address address = 
				addressFieldSetMapper.mapFieldSet(addressSubFieldSet);
		
		PersonWithNumAndAddress personWithNumAndAddress = new PersonWithNumAndAddress();
		personWithNumAndAddress.setSocialSecurityNumber(socialSecurityNumber);
		personWithNumAndAddress.setAddress(address);
		personWithNumAndAddress.setId(mainFieldSet.readLong("id"));
		personWithNumAndAddress.setFirstName(mainFieldSet.readString("firstName"));
		personWithNumAndAddress.setLastName(mainFieldSet.readString("lastName"));
		personWithNumAndAddress.setAge(mainFieldSet.readInt("age"));
		personWithNumAndAddress.setActive(mainFieldSet.readBoolean("active"));
	
		//logger.debug(">>>> via PersonWithNumAndAddressLineMapper , personWithNumAndAddress=" + personWithNumAndAddress );
		
		return personWithNumAndAddress;
	}

}
