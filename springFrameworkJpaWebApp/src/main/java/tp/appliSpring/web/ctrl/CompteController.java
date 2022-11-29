package tp.appliSpring.web.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.core.service.ServiceCompte;

@Controller  //cas particulier de @Component (pour crontroller web de spring mvc)
@SessionAttributes( value={"numClient"} ) //ou client (de classe Client) en V2
//noms des "modelAttributes" qui sont EN PLUS récupérés/stockés
//en SESSION HTTP au niveau de la page de rendu
//--> visibles en requestScope ET en sessionScope
@RequestMapping("/compte")
public class CompteController {
	
	@Autowired
	private ServiceCompte serviceCompte;
	
	
	@RequestMapping("/virement")
	public String versVirement(Model model) {
		return "virement"; //pour demander la vue jsp/virement.jsp
	}
	

	@RequestMapping("/initLogin")
	public String initLogin(Model model) {
		return "login"; //pour demander la vue jsp/login.jsp
	}
	
	
	@ModelAttribute("numClient")
	public Long addClientInModel() {
		return 0L; //valeur par defaut
	}
	

	@RequestMapping("/verifLogin")
	public String verifLogin(Model model,@RequestParam(name="numClient",required =false ) Long numClient) {
		if(numClient == null) {
			model.addAttribute("message", "numClient doit être une valeur numerique (1 ou 2 ou ...)");
			return "login"; //si rien de saisie , on réinvite à mieux saisir
		}
        model.addAttribute("numClient" , numClient); //ou objet client en v2
		return comptesDuClient(model); //même fin de traitement que route "/compteDuClient" .
	}
	
	
	@RequestMapping("/comptesDuClient")
	public String comptesDuClient(Model model) {
		Long numClient = (Long) model.getAttribute("numClient"); //ou objet "Client" en V2
		List<Compte> comptesPourClient = serviceCompte.rechercherComptesDuClient(numClient);
		model.addAttribute("listeComptes", comptesPourClient); //V1 et V2
		return "comptes"; //pour demander la vue jsp/comptes.jsp
	}

	
	@RequestMapping("/effectuerVirement")
	public String effectuerVirement(Model model,
			     @RequestParam(name="montant",required =true ) Double montant ,
			     @RequestParam(name="numCptDeb",required =true ) Long numCptDeb ,
			     @RequestParam(name="numCptCred",required =true ) Long numCptCred) {
		String message ="";
		try {
			this.serviceCompte.transferer(montant, numCptDeb, numCptCred);
			message = "virement bien effectué , montant="+montant+ 
					   " numCptDeb="+numCptDeb + " numCptCred=" + numCptCred;
			model.addAttribute("message" , message); 
			return comptesDuClient(model); //même fin de traitement que route "/compteDuClient" .
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println(e.getMessage());
			message = "echec du virement, cause="+e.getMessage();
			model.addAttribute("message" , message);
			return "virement";
		}
	}
}
