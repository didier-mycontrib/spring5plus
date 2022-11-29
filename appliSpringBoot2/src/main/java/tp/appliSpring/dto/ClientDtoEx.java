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

	

	@Override
	public String toString() {
		return "ClientDtoEx [comptes=" + comptes + ", toString()=" + super.toString() + "]";
	}



	public ClientDtoEx(Long numero, String prenom, String nom, String email, String adresse) {
		super(numero, prenom, nom, email, adresse);
	}
	
	
	public ClientDtoEx(Long numero, String prenom, String nom, String email, String adresse,List<CompteDto> comptes) {
		super(numero, prenom, nom, email, adresse);
		this.comptes = comptes;
	}
	
	
	
}
