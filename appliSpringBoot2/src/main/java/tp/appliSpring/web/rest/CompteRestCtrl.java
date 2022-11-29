package tp.appliSpring.web.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tp.appliSpring.core.exception.NotFoundException;
import tp.appliSpring.core.service.ServiceCompteWithDto;
import tp.appliSpring.dto.CompteDto;
import tp.appliSpring.dto.CompteDtoEx;
import tp.appliSpring.dto.Message;
import tp.appliSpring.dto.VirementDto;

@RestController // composant spring de type contrôleur pour Web Service REST
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = { "http://localhost:4200" , "http://www.partenaire-particulier.com" })
//@CrossOrigin(origins = "*" , methods = { RequestMethod.GET , RequestMethod.POST , RequestMethod.PUT , RequestMethod.DELETE , RequestMethod.OPTIONS})
@RequestMapping(value = "/bank-api/compte", headers = "Accept=application/json")
public class CompteRestCtrl extends AbstractGenericRestCtrl<CompteDto,CompteDtoEx,Long>{

	private ServiceCompteWithDto serviceCompteWithDto;
	
	@Autowired
	public CompteRestCtrl(ServiceCompteWithDto serviceCompteWithDto){
		super(serviceCompteWithDto);
		this.serviceCompteWithDto = serviceCompteWithDto;
	}

	/* points d'entrées hérités de AbstractGenericRestCtrl:
    ******************************************************
	GET http://localhost:8080/appliSpringBoot/bank-api/compte/1
	
	POST http://localhost:8080/appliSpringBoot/bank-api/compte
	à appeler en mode POST avec le corps de la requête HTTP comportant
	{ "numero" : null , "label" : "compteQuiVaBien" , "solde" : 50 }
	
	PUT http://localhost:8080/appliSpringBoot/bank-api/compte
	à appeler en mode PUT avec le corps de la requête HTTP comportant
	{ "numero" : 5 , "label" : "compteQuiVaEncoreBien" , "solde" : 150 }
	
	DELETE http://localhost:8080/appliSpringBoot/bank-api/compte/3
	 
	*/

	// URL= http://localhost:8080/appliSpringBoot/bank-api/compte
	// ou http://localhost:8080/appliSpringBoot/bank-api/compte?numClient=1
	// ou http://localhost:8080/appliSpringBoot/bank-api/compte?soldeMini=50
	// ou
	// http://localhost:8080/appliSpringBoot/bank-api/compte?numClient=1&soldeMini=50
	@GetMapping("")
	public List<CompteDto> getComptesByCriteria(@RequestParam(name = "numClient", required = false) Long numClient,
			@RequestParam(name = "soldeMini", required = false) Double soldeMini) {
		List<CompteDto> compteDtoList = new ArrayList<>();
		if (numClient == null && soldeMini == null) {
			compteDtoList = serviceCompteWithDto.searchAll();
		} else if (numClient != null) {
			compteDtoList = serviceCompteWithDto.searchCustomerAccounts(numClient);
			if (soldeMini != null)
				compteDtoList = compteDtoList.stream().filter((c) -> c.getSolde() >= soldeMini)
						.collect(Collectors.toList());
		} else if (soldeMini != null) {
			compteDtoList = serviceCompteWithDto.searchAccountsWithMinimumBalance(soldeMini);
		}
		return compteDtoList;
	}

	
	// URL= http://localhost:8080/appliSpringBoot/bank-api/compte/withDetails/1_or_other_id
	@GetMapping("/withDetails/{id}")
	public CompteDto getDtoByIdWithDetails(@PathVariable("id") Long id) throws NotFoundException {
		return serviceCompteWithDto.searchCompteWithOperationsById(id);
	}


	// URL= http://localhost:8080/appliSpringBoot/bank-api/compte/virement
	// appelé en mode POST avec le corps de la requête HTTP comportant
	// { "montant" : 50.0 , "numCptDeb" : "1" , "numCptCred" : "2" }
	@PostMapping("virement")
	public VirementDto postVirement(@RequestBody VirementDto virementDto) {
		try {
			serviceCompteWithDto.transfer(virementDto.getMontant(), Long.parseLong(virementDto.getNumCptDeb()),
					Long.parseLong(virementDto.getNumCptCred()));
			virementDto.setOk(true);
			virementDto.setMessage("virement bien effectué");
		} catch (Exception e) {
			virementDto.setOk(false);
			virementDto.setMessage("echec virement : " + e.getMessage());
			e.printStackTrace();
		}
		return virementDto;
	}

}
