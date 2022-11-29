package tp.appliSpring.core.init;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.core.service.ServiceCompte;

@Profile("initDataSet")
@Component
public class InitDataSet {
	
	@Autowired
	private ServiceCompte serviceCompte;
	
	@PostConstruct
	public void initDefaultDataSet() {
		serviceCompte.sauvegarderCompte(new Compte(null,"compteA",100.0));
		serviceCompte.sauvegarderCompte(new Compte(null,"compteB",150.0));
		serviceCompte.sauvegarderCompte(new Compte(null,"compteC",200.0));
		serviceCompte.sauvegarderCompte(new Compte(null,"compteD",250.0));
	}
}
