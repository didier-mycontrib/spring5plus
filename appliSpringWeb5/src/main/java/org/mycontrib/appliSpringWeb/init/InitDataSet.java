package org.mycontrib.appliSpringWeb.init;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.mycontrib.appliSpringWeb.dao.DaoOperation;
import org.mycontrib.appliSpringWeb.entity.Compte;
import org.mycontrib.appliSpringWeb.entity.Customer;
import org.mycontrib.appliSpringWeb.entity.Devise;
import org.mycontrib.appliSpringWeb.entity.News;
import org.mycontrib.appliSpringWeb.entity.Operation;
import org.mycontrib.appliSpringWeb.service.ServiceCompte;
import org.mycontrib.appliSpringWeb.service.ServiceCustomer;
import org.mycontrib.appliSpringWeb.service.ServiceDevise;
import org.mycontrib.appliSpringWeb.service.ServiceNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/*
 * classe utilitaire
 * qui initialise un jeu de données
 * au démarrage de l'application
 * Utile en mode développement si mode 
 * spring.jpa.hibernate.ddl-auto=create-drop
 */

@Component
@Profile("init")
public class InitDataSet {
	
	@Autowired
	private ServiceCompte serviceCompte;
	
	@Autowired
	private ServiceDevise serviceDevise;
	
	
	@Autowired
	private DaoOperation daoOperationJpa;
	
	@Autowired
	private ServiceCustomer serviceCustomer;
	
	@Autowired
	private ServiceNews serviceNews;
	
	@PostConstruct
	public void initData() {
		
		Customer customer1 = serviceCustomer.saveOrUpdateEntity(new Customer(null,"jean"  , "Aimare", "pwd1"));
		Customer customer2 = serviceCustomer.saveOrUpdateEntity(new Customer(null,"jean"  , "Bon", "pwd1"));
		serviceCustomer.saveOrUpdateEntity(new Customer(null,"alain"  , "Therieur", "pwd2"));
		serviceCustomer.saveOrUpdateEntity(new Customer(null,"alex"  , "Therieur", "pwd2"));
		
		Compte compteAa = new Compte(null,"compte_Aa" , 70.0);
		compteAa.setCustomer(customer1);
		compteAa = serviceCompte.saveOrUpdateEntity(compteAa);
		
		Operation op1CompteA = daoOperationJpa.save(
	    		new Operation(null,-3.2 , "achat bonbons" , new Date() , compteAa));
		
		Operation op2CompteA = daoOperationJpa.save(
	    		new Operation(null,-3.0 , "achat croissants" , new Date() , compteAa));
		
		Compte compteBbb = new Compte(null,"compte_Bbb" , 80.0);
		compteBbb.setCustomer(customer1);
		compteBbb = serviceCompte.saveOrUpdateEntity(compteBbb);
		
		Compte compteCc = new Compte(null,"compte_Cc" , -70.0);
		compteCc.setCustomer(customer2);
		compteCc = serviceCompte.saveOrUpdateEntity(compteCc);
		
		Compte compteDd =new Compte(null,"compte_Dd" , -80.0);
		compteDd.setCustomer(customer2);
		compteDd = serviceCompte.saveOrUpdateEntity(compteDd);
		
		serviceCompte.saveOrUpdateEntity(new Compte(null,"compte_EEe" , 20.0));
    	
    	serviceDevise.saveOrUpdateEntity(new Devise("EUR","Euro" , 1.0));
    	serviceDevise.saveOrUpdateEntity(new Devise("USD","Dollar" , 1.087));
    	serviceDevise.saveOrUpdateEntity(new Devise("JPY","Yen" , 158.73));
    	serviceDevise.saveOrUpdateEntity(new Devise("GBP","Livre" , 0.86));
    	
    	serviceNews.saveOrUpdateEntity(new News(null,"News 1" ));
    	serviceNews.saveOrUpdateEntity(new News(null,"News 2" ));
	}

}
