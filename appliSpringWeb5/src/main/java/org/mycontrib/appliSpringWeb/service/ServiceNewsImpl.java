package org.mycontrib.appliSpringWeb.service;

import org.mycontrib.appliSpringWeb.converter.DtoConverter;
import org.mycontrib.appliSpringWeb.dao.DaoNews;
import org.mycontrib.appliSpringWeb.entity.News;
import org.mycontrib.util.generic.service.AbstractGenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Component
@Service
@Transactional //ici (sur une classe de Service) en tant que bonne pratique
public class ServiceNewsImpl 
    extends AbstractGenericService<News,Long>
    implements ServiceNews {
	
	public DtoConverter dtoConverter = new DtoConverter();//for specific convert
	
	@Autowired
	private DaoNews daoNews;
	
	@Override
	public CrudRepository<News,Long> getMainDao() {
		return this.daoNews;
	}
	
	
	Logger logger = LoggerFactory.getLogger(ServiceNewsImpl.class);

	@Override
	public void initEntityRelationShipsFromDtoBeforeSave(News entity, Object dto) {	
	}
	
	

}
