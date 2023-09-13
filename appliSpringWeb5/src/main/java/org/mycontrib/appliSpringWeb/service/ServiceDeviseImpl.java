package org.mycontrib.appliSpringWeb.service;

import org.mycontrib.appliSpringWeb.dao.DaoDevise;
import org.mycontrib.appliSpringWeb.entity.Devise;
import org.mycontrib.util.generic.service.AbstractGenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceDeviseImpl 
    extends AbstractGenericService<Devise, String>
    implements ServiceDevise {
	
	@Override
	public CrudRepository<Devise, String> getMainDao() {
		return this.daoDevise;
	}
	

	Logger logger = LoggerFactory.getLogger(ServiceDeviseImpl.class);
	
	
	@Autowired
	private DaoDevise daoDevise; //dao principal


	@Override
	public void initEntityRelationShipsFromDtoBeforeSave(Devise entity, Object dto) {
	}
	
}
