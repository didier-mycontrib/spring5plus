package tp.tpSpringBatch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@PropertySource("classpath:application.properties") 
@EnableBatchProcessing()
public class JobRepositoryBatchConfig {
	
	@Value("${spring.datasource.driverClassName}")
	private String jdbcDriver;
	
	@Value("${spring.datasource.url}")
	private String dbUrl;
	
	@Value("${spring.datasource.username}")
	private String dbUsername;
	
	@Value("${spring.datasource.password}")
	private String dbPassword;
	
	/*
	 NB: in application.properties
	 NOT specific  spring.batch.datasource.url=jdbc:h2:mem:jobRepositoryDb
	 BUT (default springBoot main dataSource):
	      spring.datasource.url=jdbc:h2:~/jobRepositoryDb
	      spring.datasource.username=sa
          spring.datasource.password=
	 */
	
	
	 
	
	 @Bean(value = "dataSource") @Qualifier("jobRepositoryDb")
	 @Primary
		public DataSource dataSource() {
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(jdbcDriver);
			dataSource.setUrl(dbUrl);
			dataSource.setUsername(dbUsername);
			dataSource.setPassword(dbPassword);		
			return dataSource;
		}
		
	 
	 @Bean(name="transactionManager") @Qualifier("batch")
	  public PlatformTransactionManager batchTransactionManager(
	                @Qualifier("jobRepositoryDb") DataSource batchDataSource) {
	    return new DataSourceTransactionManager(batchDataSource);
	  }
     
	 @Bean(name = "jobRepository")
	  public JobRepository jobRepository(
			  @Qualifier("jobRepositoryDb") DataSource  batchDataSource,
			  @Qualifier("batch") PlatformTransactionManager batchTransactionManager) throws Exception {
	      JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
	      factory.setDataSource(batchDataSource);
	      factory.setTransactionManager(batchTransactionManager);
	      factory.afterPropertiesSet();
	      return factory.getObject();
	  }
	  
	
	
	  @Bean()
	  public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
	     //TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher(); //Spring batch 5 only , not v4
		 SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
	     jobLauncher.setJobRepository(jobRepository);
	     jobLauncher.afterPropertiesSet();
	     return jobLauncher;
	  }
	  
	  //@Bean @Qualifier("batch")
	  public DataSourceInitializer databasePopulator(
			  @Qualifier("jobRepositoryDb") DataSource  batchDataSource) {
	    ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
	    //populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-drop-h2.sql"));
	    populator.addScript(new ClassPathResource("org/springframework/batch/core/schema-h2.sql"));
	    populator.setContinueOnError(false);    populator.setIgnoreFailedDrops(false);
	    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
	    dataSourceInitializer.setDataSource(batchDataSource);
	    dataSourceInitializer.setDatabasePopulator(populator);
	    return dataSourceInitializer;
	  }
	  
  
  
}
