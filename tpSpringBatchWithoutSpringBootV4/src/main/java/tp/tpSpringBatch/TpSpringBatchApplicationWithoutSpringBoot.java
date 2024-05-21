package tp.tpSpringBatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.stereotype.Component;


//version sans springBoot
@Component
public class TpSpringBatchApplicationWithoutSpringBoot {
	
	private final JobLauncher jobLauncher;
	private final ApplicationContext applicationContext;
	
	@Autowired
	public TpSpringBatchApplicationWithoutSpringBoot(JobLauncher jobLauncher,
			                        ApplicationContext applicationContext)  {
	    this.jobLauncher = jobLauncher;
	    this.applicationContext = applicationContext;
	}

	/*
	 NB: sans automatisme springBoot/spring.batch[.jdbc].initialize-schema=always
	 il faut SEULEMENT au PREMIER LANCEMENT décommenter  //@Bean @Qualifier("batch")
	 au dessus de
	  public DataSourceInitializer databasePopulator ...
	  dans tp.tpSpringBatch.config.JobRepositoryBatchConfig
	 */
	
	public static void main(String[] args) throws Exception {
		//String defaultProfils  = "xmlJobConfig";
		String defaultProfils  = "";
		System.setProperty("spring.profiles.default", defaultProfils);
		

		ApplicationContext /*AnnotationConfigApplicationContext*/ springContext = new
				AnnotationConfigApplicationContext(MyMainSpringBatchConfig.class) ;

		TpSpringBatchApplicationWithoutSpringBoot app = springContext.getBean(TpSpringBatchApplicationWithoutSpringBoot.class);
		
		app.run(args);
	}
	
	
	public void run(String... args) throws Exception {
	  Job job = (Job) applicationContext.getBean("myHelloWorldJob");
	  System.out.println("lauching job = " + job.getName());
	 
	 JobParameters jobParameters = new JobParametersBuilder()
			 .addLong("timeStampOfJobInstance", System.currentTimeMillis())//Necessary for running several instances of a same job (each jobInstance must have a parameter that changes)
			 .toJobParameters();
	 var jobExecution = jobLauncher.run(job, jobParameters);
	 System.out.println("jobExecution = " + jobExecution);
	 
	 var batchStatus = jobExecution.getStatus();
	 System.out.println("batchStatus = " + batchStatus);
	 while (batchStatus.isRunning()) {
	      System.out.println("Job still running...");
	      Thread.sleep(5000L);
	    }
	 System.out.println("Job is finished ...");
	 }
}
