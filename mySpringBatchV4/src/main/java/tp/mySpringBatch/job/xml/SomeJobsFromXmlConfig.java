package tp.mySpringBatch.job.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("xmlJobConfig")
@ImportResource({"classpath:job/commonConfig.xml",
	             "classpath:job/myHelloWorldJob.xml",
	             "classpath:job/myHelloWorldWithParameterJob.xml",
	             "classpath:job/insertIntoDbFromCsvJob.xml",
	             "classpath:job/insertIntoCsvFromDbJob.xml",
	             "classpath:job/copyFromCsvToCsvJob.xml",
	             "classpath:job/fromCsvToXmlJob.xml",
	             "classpath:job/fromCsvToFixedPosTxtJob.xml",
	             "classpath:job/fromFixedPosTxtToCsvJob.xml",
	             "classpath:job/fromJsonToXmlJob.xml",
	             "classpath:job/fromXmlToCsvJob.xml",
	             "classpath:job/fromCsvToJsonJob.xml",
	             "classpath:job/withMyTaskletJob.xml",
	             "classpath:job/myFlowJobs.xml",
	             "classpath:job/myPartitionJob.xml"})
public class SomeJobsFromXmlConfig {

  public static final Logger logger = LoggerFactory.getLogger(SomeJobsFromXmlConfig.class);

  


}