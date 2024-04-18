package tp.mySpringBatch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SocialSecurityNumber {
	
	private String genre ; // sur 1 caractère ("1" pour Homme et "2" pour Femme  )
	private String anneeNaissance; //sur 2 caractères
	private String numMoisNaissance; // sur 2 caractères
	private String numDepartementNaissance; // sur 2 caractères
	private String numCommuneNaissance; // sur 3 caractères
	private String numOrdreNaissance; //sur 3 caractères
	private String clef; //de sécurité/cohérence sur 2 caractères
    //en tout : 1 + 2 + 2 + 2 + 3 + 3 + 2 =  13 + 2 = 15 chiffres
	
	
	public SocialSecurityNumber(String numSecu) {
		if(numSecu.length()>=13) {
			this.genre = numSecu.substring(0,1);
			this.anneeNaissance = numSecu.substring(1,3);
			this.numMoisNaissance = numSecu.substring(3,5);
			this.numDepartementNaissance = numSecu.substring(5,7);
			this.numCommuneNaissance = numSecu.substring(7,10);
			this.numOrdreNaissance = numSecu.substring(10,13);
		}
		if(numSecu.length()==15) {
		    this.clef = numSecu.substring(13,15);
		}else {
			this.clef = this.computeKey();
		}
	}

	@Override
	public String toString() {
		return "SocialSecurityNumber [genre=" + genre + ", anneeNaissance=" + anneeNaissance + ", numMoisNaissance="
				+ numMoisNaissance + ", numDepartementNaissance=" + numDepartementNaissance + ", numCommuneNaissance="
				+ numCommuneNaissance + ", numOrdreNaissance=" + numOrdreNaissance + ", clef=" + clef + "]";
	}
	
	public String toGlobalStringWhithKey() {
		return  genre +  anneeNaissance + numMoisNaissance +  numDepartementNaissance + numCommuneNaissance
				+ numOrdreNaissance + clef ;
	}
	
	public String toGlobalStringWhithoutKey() {
		return  genre +  anneeNaissance + numMoisNaissance +  numDepartementNaissance + numCommuneNaissance
				+ numOrdreNaissance ;
	}
	
	public String computeKey() {
		Long ssnWithoutKey = Long.parseLong(this.toGlobalStringWhithoutKey());
		long key = 97 - (ssnWithoutKey % 97);
		return String.valueOf(key);
	}

	
	
	
	

}
