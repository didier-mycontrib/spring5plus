package org.mycontrib.appliSpringWeb.rest;

import org.mycontrib.appliSpringWeb.dto.VirementRequest;
import org.mycontrib.appliSpringWeb.dto.VirementResponse;
import org.mycontrib.appliSpringWeb.entity.Compte;
import org.mycontrib.appliSpringWeb.service.ServiceCompte;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/rest/api-bank/virement" , 
                headers="Accept=application/json")
public class VirementRestCtrl {
	
	@Autowired
	private ServiceCompte serviceCompte;
	
	//exemple de fin d'URL: ./rest/api-bank/virement
	//appelé en mode POST avec dans la partie invisible "body" de la requête:
	// { "numCompteDebit" : 1 , "numCompteCredit" : 2 , "montant" : 50.0 }
	@PostMapping("" )
	@PreAuthorize("hasRole('ROLE_CUSTOMER')")
	//pour affiner ultérieurement la sécurité: virement autorisé que si le compte à débiter appartient au client connecté .
	public VirementResponse postVirement(@RequestBody VirementRequest virementRequest) {
		    VirementResponse virementResponse = new VirementResponse();
		    /*
		    virementResponse.setMontant(virementRequest.getMontant());
		    virementResponse.setNumCompteDebit(virementRequest.getNumCompteDebit());
		    virementResponse.setNumCompteCredit(virementRequest.getNumCompteCredit());
		    */
		    BeanUtils.copyProperties(virementRequest, virementResponse);
		    try {
		        serviceCompte.transferer(virementRequest.getMontant(), 
		        		                 virementRequest.getNumCompteDebit(), 
		        		                 virementRequest.getNumCompteCredit());
				
				virementResponse.setStatus(true);
				virementResponse.setMessage("virement bien effectué");
			} catch (Exception e) {
				//e.printStackTrace();
				virementResponse.setStatus(false);
				virementResponse.setMessage("echec virement " + e.getMessage());
			}	
			return virementResponse;
	}

}
