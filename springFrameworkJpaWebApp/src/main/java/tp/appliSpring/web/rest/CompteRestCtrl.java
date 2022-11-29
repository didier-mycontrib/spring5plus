package tp.appliSpring.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.core.service.ServiceCompte;
import tp.appliSpring.web.dto.CompteDto;

@RestController
@RequestMapping(value = "/bank-api/compte" , headers = "Accept=application/json" )
public class CompteRestCtrl {
	
	@Autowired
	private ServiceCompte serviceCompte;
	
	public CompteRestCtrl() {
		System.out.println("CompteRestCtrl load ...");
	}

	//http://localhost:appSpring/mvc/bank-api/compte
	@GetMapping("")
	public List<CompteDto> getCompteByCriteria() {
		List<CompteDto> listeCompteDto = new ArrayList<>();
		/*
		listeComptes.add(new CompteDto(1L,"compteA",100.0));
		listeComptes.add(new CompteDto(2L,"compteB",150.0));
		*/
		List<Compte> listeComptes = serviceCompte.rechercherTousComptes();
		for(Compte c: listeComptes) {
			listeCompteDto.add(new CompteDto(c.getNumero(),c.getLabel(),c.getSolde()));
		}
		return listeCompteDto;
	}

}
