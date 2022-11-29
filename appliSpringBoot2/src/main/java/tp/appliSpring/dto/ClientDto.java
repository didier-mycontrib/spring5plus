package tp.appliSpring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//cette classe "Customer" ou "ClientDto" ou ...
//correspond à un DTO (Data Transfert Object):
//un objet de données qui sera transférer à travers le réseau ou entre couches logicielles
// entity.Client --> dto.Customer
// dto.Customer --> entity.Client
@Getter @Setter @ToString @NoArgsConstructor
public class ClientDto {
   private Long numero;
   private String prenom;
   private String nom;
   private String email;
   private String adresse;
   
   public ClientDto(Long numero, String prenom, String nom, String email, String adresse) {
		super();
		this.numero = numero;
		this.prenom = prenom;
		this.nom = nom;
		this.email = email;
		this.adresse = adresse;
   }
}
