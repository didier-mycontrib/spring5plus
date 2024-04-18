package tp.mySpringBatch.datasource;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class MyOutputDbDataSourceConfig {

	/*
	 NB: in application.properties
	 NOT spring.datasource.url=jdbc:h2:mem:outputDb
	 BUT spring.outputdb.datasource.url=jdbc:h2:~/outputDb
	 and spring.outputdb.datasource.username=sa
        spring.outputdb.datasource.password=
	 */
	@Bean @Qualifier("outputdb")
  @ConfigurationProperties("spring.outputdb.datasource")
  public DataSourceProperties outputdbDataSourceProperties() {
      return new DataSourceProperties();
  }
	
	@Bean(name="outputdbDataSource") @Qualifier("outputdb")
	//@BatchDataSource
	public DataSource outputdbDataSource(@Qualifier("outputdb") DataSourceProperties outputdbDataSourceProperties) {
	    return outputdbDataSourceProperties
	      .initializeDataSourceBuilder()
	      .build();
	}
	
	//pour préparer (créer) la table dans la base outputdb
		 @Bean
		  public DataSourceInitializer databaseInitializer(@Qualifier("outputdb") DataSource outputdbDataSource) {
		    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		    populator.addScript(new ClassPathResource("sql/batch-schema.sql"));
		    populator.setContinueOnError(false);
		    populator.setIgnoreFailedDrops(false);
		    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		    dataSourceInitializer.setDataSource(outputdbDataSource);
		    dataSourceInitializer.setDatabasePopulator(populator);
		    return dataSourceInitializer;
		  }

	
}
