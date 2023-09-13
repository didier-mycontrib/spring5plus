package org.mycontrib.appliSpringWeb.service;

import java.util.List;

import org.mycontrib.appliSpringWeb.dto.CustomerL0;
import org.mycontrib.appliSpringWeb.entity.Customer;
import org.mycontrib.util.generic.service.GenericService;

//Business service / service métier
//avec remontées d'exceptions (héritant de RuntimeException)
public interface ServiceCustomer extends GenericService<Customer,Long>{
	//principales méthodes héritées:
	/*
	 Customer searchById(long idCustomer); //retournant le Customer seulement
	 Customer saveOrUpdate(Customer customer);
	 void deleteById(long idCustomer);
	 boolean existById(long idCustomer);
	 */
	
	//méthode spécifique au métier de la banque 
	boolean checkCustomerPassword(long customerId,String password);
	String resetCustomerPassword(long customerId);//genere et affecte nouveau mot de passe temp.
	//...
	//méthodes déléguées aux DAOs le CRUD:
	
	Customer rechercherCustomerAvecComptesParNumero(long idCustomer); //sans lazy exception
	
	//utile pour voir d'eventuels doublons:
	List<Customer> rechercherCustomerSelonPrenomEtNom(String prenom,String nom); 

	//...
}
