package tp.appliSpring.core.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement() // "transactionManager" (not "txManager") is expected !!!
@ComponentScan(basePackages = { "tp.appliSpring.core.dao" ,  "tp.appliSpring.core.service" ,  "tp.appliSpring.core.init"})
public class DomainAndPersistenceConfig {

	
	// Transaction Manager for JDBC or ...
	@Bean(name = "transactionManager") 
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		JdbcTransactionManager txManager = new JdbcTransactionManager();
		//DataSourceTransactionManager txManager = new DataSourceTransactionManager();
		txManager.setDataSource(dataSource);
		return txManager;
	}

}
