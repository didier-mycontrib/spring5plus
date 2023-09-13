package org.mycontrib.appliSpringWeb.service;

import org.mycontrib.appliSpringWeb.entity.Devise;
import org.mycontrib.util.generic.service.GenericService;

//Business service / service métier
//avec remontées d'exceptions (héritant de RuntimeException)
public interface ServiceDevise extends GenericService<Devise,String> {

	
	//méthodes déléguées aux DAOs le CRUD:
	//Devise searchById(String code); //hérité de GenericService
	//Devise saveOrUpdate(Devise devise);//hérité de GenericService
	
	
	//void deleteById(String code);//hérité de GenericService
	//boolean existById(String code);//hérité de GenericService
	//searchAll() et searchAllDto(dtoClass) hérité de GenericService
}
