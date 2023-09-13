package org.mycontrib.appliSpringWeb.service;

import org.mycontrib.appliSpringWeb.entity.News;
import org.mycontrib.util.generic.service.GenericService;

//Business service / service métier
//avec remontées d'exceptions (héritant de RuntimeException)
public interface ServiceNews extends GenericService<News,Long> {
	
}
