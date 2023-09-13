package org.mycontrib.appliSpringWeb.dao;

import org.mycontrib.appliSpringWeb.entity.Devise;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * DAO = Data Access Object
 * avec méthode CRUD
 * et throws RuntimeException implicites
 */

public interface DaoDevise extends JpaRepository<Devise,String> {
	 /*
    .save()
    .findAll() 
    .findById()
    .deleteById() 
    héritées de JpaRepository / CrudRepository
    */
	
}
