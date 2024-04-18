package tp.mySpringBatch.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyInputDbDataSourceConfig {
	/*
	 NB: in application.properties
	 NOT spring.datasource.url=jdbc:h2:mem:inputDb
	 BUT spring.inputdb.datasource.url=jdbc:h2:~/inputDb
	 and spring.inputdb.datasource.username=sa
        spring.inputdb.datasource.password=
	 */
	@Bean @Qualifier("inputdb")
 @ConfigurationProperties("spring.inputdb.datasource")
 public DataSourceProperties inputdbDataSourceProperties() {
     return new DataSourceProperties();
 }
	
	@Bean(name="inputdbDataSource") @Qualifier("inputdb")
	//@BatchDataSource
	public DataSource inputdbDataSource(@Qualifier("inputdb") DataSourceProperties inputdbDataSourceProperties) {
	    return inputdbDataSourceProperties
	      .initializeDataSourceBuilder()
	      .build();
	}
	
}
