package tp.mySpringBatch.db;

import java.sql.Connection;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
//@Profile("?")
public class MyInputDbInitializer {
	
	public static final Logger logger = LoggerFactory.getLogger(MyInputDbInitializer.class);
	 
	
	@Autowired(required = false) 
	@Qualifier("inputdb") 
	DataSource inputdbDataSource;
	
	@PostConstruct
	public void initDataSetIfNotExists() {
		if(inputdbDataSource==null) return;
		try(Connection connection = inputdbDataSource.getConnection()){
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		    populator.addScript(new ClassPathResource("sql/select-persons.sql"));
		    populator.setContinueOnError(false);
		    populator.populate(connection);
		    logger.info("MyInputDbInitializer.initDataSetIfNotExists() : already initialized");
		}
		catch(Exception ex) {
			//INIT TABLE and DATASET IF NOT EXISTS:
			initDataSet();
			//ex.printStackTrace();
		}
	}
	
	
	public void initDataSet() {
		if(inputdbDataSource==null) return;
		try(Connection connection = inputdbDataSource.getConnection()){
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		    populator.addScript(new ClassPathResource("sql/batch-schema.sql"));
		    populator.addScript(new ClassPathResource("sql/insert-persons.sql"));
		    populator.setContinueOnError(false);
		    populator.setIgnoreFailedDrops(false);
		    populator.populate(connection);
		    logger.info("MyInputDbInitializer.initDataSet() : table person initialized with some data");
		}
		catch(Exception ex) {
			logger.error("MyInputDbInitializer.initDataSet() failure:" + ex.getMessage());
		}
	}

}
