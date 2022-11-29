package tp.appliSpring.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class OperationDto {
	private Long numOp;
	private String label;
	private Double montant;
	private String dateOp;
	
	public OperationDto(Long numOp,String label, Double montant, String dateOp) {
		super();
		this.numOp=numOp;
		this.label = label;
		this.montant = montant;
		this.dateOp = dateOp;
	}
	
	public OperationDto(Long numOp,String label, Double montant, Date date) {
		super();
		this.numOp=numOp;
		this.label = label;
		this.montant = montant;
		this.dateOp = (new SimpleDateFormat("yyy-MM-dd")).format(date);
	}
	
	public OperationDto(Long numOp,String label, Double montant) {
		this(numOp, label,montant,new Date());
	}
	
	
	
}
