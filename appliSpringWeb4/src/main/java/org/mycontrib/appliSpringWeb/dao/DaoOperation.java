package org.mycontrib.appliSpringWeb.dao;

import org.mycontrib.appliSpringWeb.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * DAO = Data Access Object
 * avec méthode CRUD
 * et throws RuntimeException implicites
 */

public interface DaoOperation extends JpaRepository<Operation,Long> {
   
}
