package tp.appliSpring.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import tp.appliSpring.core.entity.Client;
import tp.appliSpring.core.service.ServiceClientWithDto;
import tp.appliSpring.core.service.ServiceCompteWithDto;
import tp.appliSpring.dto.ClientDto;
import tp.appliSpring.dto.CompteDto;
import tp.appliSpring.web.form.VirementForm;

@Controller //composant spring de type Crontroller spring-mvc
@RequestMapping("/site/compte")
@SessionAttributes( value={"client", "comptes"} )
//noms des "modelAttributes" qui sont EN PLUS récupérés/stockés
// en SESSION HTTP au niveau de la page de rendu 
// --> visibles en requestScope ET en sessionScope
public class CompteController {
	
	@Autowired
	private ServiceCompteWithDto serviceCompte;
	
	@Autowired
	private ServiceClientWithDto serviceClient;
	
	@ModelAttribute("client") //NB: cette méthode n'est pas appelée/déclenchée
	//si "client" est déjà présent en session (et par copie) dans le modèle
	public Client addClientAttributeInModel() {
	   return new Client() ;
	}

	//pour  form:form , form:select , ...
    @ModelAttribute("virementForm")
	public VirementForm addConvAttributeInModel() {
	        return new VirementForm();
	}

	
	@RequestMapping("/to-virement")
	 public String versVirement(Model model) {
	    return "virement"; //aiguiller sur la vue "virement"
	 }
	
	@RequestMapping("/verifLogin")
	 public String verifLogin(Model model,@RequestParam(name="numClient")  Long numClient) {
		System.out.println("/site/compte/verifLogin with numClient="+numClient);
		List<CompteDto> comptes = serviceCompte.searchCustomerAccounts(numClient);
		ClientDto client = serviceClient.searchById(numClient);
	    model.addAttribute("client", client);
	    model.addAttribute("comptes", comptes);
	    return "comptes"; //aiguiller sur la vue "comptes"
	 }
	
	
	
	@RequestMapping("/doVirement")
	 public String effectuerVirement(Model model,
			 @ModelAttribute("virementForm") @Valid VirementForm virementForm , 
             BindingResult bindingResult,
             HttpSession session) {
		if (bindingResult.hasErrors()) {
		    // form validation error
			System.out.println("form validation error: " + bindingResult.toString());
			return "virement";
		}
		/*else*/
		String message="";
		try {
			serviceCompte.transfer(virementForm.getMontant(),
					               virementForm.getNumCptDeb(), 
					               virementForm.getNumCptCred());
			message="virement bien effectué";
		} catch (Exception e) {
			message="echec virement: " + e.getMessage();
			e.printStackTrace();
		}
		ClientDto client = (ClientDto) model.getAttribute("client");
		List<CompteDto> comptes = serviceCompte.searchCustomerAccounts(client.getNumero());
		model.addAttribute("title","comptes");
	    model.addAttribute("client", client);
	    model.addAttribute("comptes", comptes);
	    model.addAttribute("message", message);
	    return "comptes"; //aiguiller sur la vue "comptes"
	 }
}
