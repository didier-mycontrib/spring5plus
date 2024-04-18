package tp.mySpringBatch.model;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "stat") //just for read/generate XML file wkith jaxb2 marshaller
public class EmployeeStat {

	private String function;
	private Long numberOfEmployees;
	private Double minSalary;
	private Double maxSalary;
	private Double averageSalary;

}
