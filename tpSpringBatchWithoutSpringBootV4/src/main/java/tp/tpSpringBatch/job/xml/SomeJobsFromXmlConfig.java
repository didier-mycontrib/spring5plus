package tp.tpSpringBatch.job.xml;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("xmlJobConfig")
@ImportResource({"classpath:job/commonConfig.xml",
	             "classpath:job/myHelloWorldJob.xml"})
public class SomeJobsFromXmlConfig {
}