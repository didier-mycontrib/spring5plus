package tp.mySpringBatch.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;




@Configuration
//Surtout pas ici de @EnableBatchProcessing(dataSourceRef = ... ,transactionManagerRef = ...)
//SIMPLE eventuel complement ou redéfinition partielle de la 
//configuration automatique apportée par spring-boot-starter-batch
public class AutomaticSpringBootBatchJobRepositoryConfig {
	
	/*
	 NB: in application.properties
	 NOT specific  spring.batch.datasource.url=jdbc:h2:mem:jobRepositoryDb
	 BUT (default springBoot main dataSource):
	      spring.datasource.url=jdbc:h2:~/jobRepositoryDb
	      spring.datasource.username=sa
          spring.datasource.password=
	 */
	
	
	/*
	 NB1: default DataSource and TransactionManager will be configure/created by spring-boot-starter-batch
	 */
	
	/*
	 NB2: No need of @Bean DataSourceInitializer databasePopulator() for initialize jobRepository DataBase
	 via org/springframework/batch/core/schema-drop-h2.sql and org/springframework/batch/core/schema-h2.sql
	 BECAUSE 
	 spring.batch.jdbc.initialize-schema=always
	 IN application.properties WILL DO THAT automatically at first start
	 WITH DEFAULT DataSource (of SpringBatch)
	 */
	
	/*
	 NB3 : JobRepository will be automatically build by spring-boot-starter-batch
	       on top of default dataSource and transactionManager
	       
	       JobLauncher will be automatically build by spring-boot-starter-batch
	       on top of JobRepository
	       
	       IN ORDER TO BUILD THAT , SpringBoot (as a default config) need to build himself 
	       the default dataSource and transaction manager
	       --> No Other DataSource should be detected at startup of SpringBatchApp on top of spring-boot-starter-batch !!!
	       --> Or If several Datasources JUST ONE DataSource (With @Primary and @BatchDataSource) 
	           need to be explicitly configure (with same behavior of springBatch default)
	           in order to enable spring-boot-starter-batch to build is component stack .
	 */

	
	 @Bean @Qualifier("jobRepositoryDb")
	 @ConfigurationProperties("spring.datasource")
	 //@ConfigurationProperties("spring.batch.datasource") //possible mais moins classique
	 public DataSourceProperties jobRepositoryDbDataSourceProperties() {
	     return new DataSourceProperties();
	 }
		
	//NB: cette configuration explicite du "datasource" principal (@Primary) 
	 //   à utiliser par springBatch et springBoot (@BatchDataSource)
	 // n'est utile que pour préciser la configuration "Datasource" prioritaire (sans conflit avec les autres)
	 @Primary
	 @BatchDataSource
	 @Bean(value = "dataSource")
	    public DataSource datasource(@Qualifier("jobRepositoryDb") DataSourceProperties dbDataSourceProperties) {
		  return dbDataSourceProperties
			      .initializeDataSourceBuilder()
			      .build();
	    }
     
	
	
	/*
	 NB4: A batch application is just a consoleApp who have the unique responsability
	      of do batch_processing .
	      No user , no gui , no web part .
	      So: it is not a problem if the default/main datasource/transaction manager are used for springBatch
	      
	      Even if the springBatch job need to use a other database (with a specific other dataSource)
	      the transaction need is for springBatch works (commit interval,...)
	 */
	
	/*
	 NB5 : https://www.baeldung.com/spring-boot-spring-batch 
	 for "SpringBatch" within "springBoot"
	 */
  
  
}
