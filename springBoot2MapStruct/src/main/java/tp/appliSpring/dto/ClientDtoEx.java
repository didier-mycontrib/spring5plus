package tp.appliSpring.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor
public class ClientDtoEx extends ClientDto {
	private List<CompteDto> comptes = new ArrayList<>();

	public ClientDtoEx(Long number, String firstName, String lastName, String email, String address) {
		super(number, firstName, lastName, email, address);
	}
	
	public ClientDtoEx(Long number, String firstName, String lastName, String email, String address,List<CompteDto> comptes) {
		super(number, firstName, lastName, email, address);
		this.comptes=comptes;
	}

	@Override
	public String toString() {
		return "ClientDtoEx [comptes=" + comptes + ", toString()=" + super.toString() + "]";
	}
	
	
	
	
}
