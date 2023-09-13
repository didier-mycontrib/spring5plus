package org.mycontrib.appliSpringWeb.dao;

import org.mycontrib.appliSpringWeb.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DaoNews extends JpaRepository<News,Long>{

}
