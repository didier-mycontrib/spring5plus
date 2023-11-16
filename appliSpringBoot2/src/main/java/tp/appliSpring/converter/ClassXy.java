package tp.appliSpring.converter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class ClassXy {
	private Integer id;
	private String label;
	
	public ClassXy(Integer id, String label) {
		super();
		this.id = id;
		this.label = label;
	}
	
	
}
