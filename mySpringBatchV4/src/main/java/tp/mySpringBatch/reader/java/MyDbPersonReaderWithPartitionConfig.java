package tp.mySpringBatch.reader.java;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import tp.mySpringBatch.db.PersonRowMapper;
import tp.mySpringBatch.model.Person;

@Configuration
@Profile("!xmlJobConfig")
public class MyDbPersonReaderWithPartitionConfig {
	
		 
	 @Bean @Qualifier("db_withPartition")
	 @StepScope
	  ItemReader<Person> jdbcPartitionPersonReader(
			  @Qualifier("inputdb") DataSource inputdbDataSource,
			  @Value("#{stepExecutionContext[fromId]}") String fromId,
			  @Value("#{stepExecutionContext[toId]}") String toId
			  ) throws Exception {
		 
		     SqlPagingQueryProviderFactoryBean pagingQueryProviderFactory = new SqlPagingQueryProviderFactoryBean();
		     pagingQueryProviderFactory.setDataSource(inputdbDataSource);
		     pagingQueryProviderFactory.setSelectClause("select id, first_name, last_name, age, is_active");
		     pagingQueryProviderFactory.setFromClause("from person");
		     pagingQueryProviderFactory.setWhereClause("where id >= :fromId and id <= :toId");
		     pagingQueryProviderFactory.setSortKey("id");
		     PagingQueryProvider pagingQueryProvider = pagingQueryProviderFactory.getObject();
		    		 
		     Map<String,Object> parameterValues = new HashMap<>();
		     parameterValues.put("fromId", fromId);
		     parameterValues.put("toId", toId);
		 
			 return new JdbcPagingItemReaderBuilder<Person>()
			  .name("jdbcPartitionPersonReader")
			  .dataSource(inputdbDataSource)
			  .queryProvider(pagingQueryProvider)
			  .parameterValues(parameterValues)
			  .pageSize(5)
			  .rowMapper(new PersonRowMapper())
			  .build();
		
		}
	 
	 //TEMPORAIRE(premier lancement) pour préparer (créer) la table et un jeu de données dans la base inputdb
	 //old inputDatabaseInitializer() now replaced by db.MyInputDbInitializer
	
}
