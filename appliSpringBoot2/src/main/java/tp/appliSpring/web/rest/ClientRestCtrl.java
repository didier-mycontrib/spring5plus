package tp.appliSpring.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import tp.appliSpring.core.service.ServiceClientWithDto;
import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.ClientDtoEx;
import tp.appliSpring.dto.Message;

@RestController //composant spring de type contrôleur pour Web Service REST
@CrossOrigin(origins = "*")
//@CrossOrigin(origins = { "http://localhost:4200" , "http://www.partenaire-particulier.com" })
//@CrossOrigin(origins = "*" , methods = { RequestMethod.GET , RequestMethod.POST , RequestMethod.PUT , RequestMethod.DELETE , RequestMethod.OPTIONS})
@RequestMapping(value="/bank-api/client" , headers="Accept=application/json")
public class ClientRestCtrl extends AbstractGenericRestCtrlWithoutMapping<ClientDto,ClientDtoEx,Long> {
	
	
	private ServiceClientWithDto serviceClientWithDto;
	
	@Autowired
	public ClientRestCtrl(ServiceClientWithDto serviceClientWithDto){
		super(serviceClientWithDto);
		this.serviceClientWithDto = serviceClientWithDto;
	}
	
	//URL= http://localhost:8080/appliSpringBoot/bank-api/client/1
	@GetMapping("/{numClient}")
	public ClientDto getCustomerByNum(@PathVariable("numClient") Long numClient) {
			return super.getDtoById(numClient);
	}
	
	
	
	//URL= http://localhost:8080/appliSpringBoot/bank-api/client
	//ou   http://localhost:8080/appliSpringBoot/bank-api/client?nom=Therieur
	@GetMapping("")
	public List<ClientDto> getCustomersByCriteria(
			@RequestParam(name="nom" , required=false)String nomClient){
		if(nomClient==null)
			return serviceClientWithDto.searchAll();
		else {
		   // return serviceClientWithDto.searchByLastName(nomClient);
		   return serviceClientWithDto.searchAll().stream()
				  .filter((client)->client.getNom().equals(nomClient))
				  .collect(Collectors.toList());
		}
	}
	
	
	//URL= http://localhost:8080/appliSpringBoot/bank-api/client
	//appelé en mode POST avec le corps de la requête HTTP comportant
	// { "number" : null , "firstName" :  "jean" , "lastName" : "Bon" , "address" : "12 rue Xy Paris" , "email" : "jean.Bon@gmail.com"}
	@PostMapping("") 
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ClientDto postCustomer(@RequestBody ClientDto clientDto) {
		return super.postDto(clientDto);
	}
	
	//URL= http://localhost:8080/appliSpringBoot/bank-api/client
	//appelé en mode PUT avec le corps de la requête HTTP comportant
	// { "number" : 4 , "firstName" :  "jean2" , "lastName" : "Bon" , "address" : "13 rue Xy Paris" , "email" : "jean.Bon@gmail.com"}
	@PutMapping("") 
	public ClientDto putCustomer(@RequestBody ClientDto clientDto) {
		return super.putDto(clientDto);
	}
	
	//URL= http://localhost:8080/appliSpringBoot/bank-api/client/3
	//appelé en mode delete
	@DeleteMapping("/{numClient}")
	public ResponseEntity<Message> deleteCustomerByNum(@PathVariable("numClient") Long numClient) {
		return super.deleteDtoById(numClient);
	}

}
