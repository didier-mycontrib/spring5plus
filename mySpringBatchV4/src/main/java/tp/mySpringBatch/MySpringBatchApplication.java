package tp.mySpringBatch;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication()
@EnableBatchProcessing //nécessaire avec SpringBoot2/SpringBatch4 (pas absoluement nécessaire avec springBoot3/springBatch5)
public class MySpringBatchApplication implements CommandLineRunner {
	
  private String defaultJobName ; //to launch (set this value in initDefaultJobName())
	
  public static final Logger logger = LoggerFactory.getLogger(MySpringBatchApplication.class);

  private final JobLauncher jobLauncher;
  private final ApplicationContext applicationContext;
  private static boolean launchRunAfterMain=false;
  
  public static void initProfiles() {
		//java .... -Dspring.profiles.active=reInit,dev
		String profilsActifs  = System.getProperty("spring.profiles.active");
		if(profilsActifs!=null) { 
			System.out.println("spring.profiles.active="+profilsActifs);
		}else {
			//String defaultProfils  = "xmlJobConfig";
			String defaultProfils  = "";
			System.setProperty("spring.profiles.default", defaultProfils);
			System.out.println("spring.profiles.default="+defaultProfils);
		}
	}

  @Autowired
  public MySpringBatchApplication(JobLauncher jobLauncher,
		                          ApplicationContext applicationContext) {
	//injection by constructor  
    this.jobLauncher = jobLauncher;
    this.applicationContext = applicationContext;
  }

  public static void main(String[] args) {
		initProfiles();
		launchRunAfterMain=true;
		SpringApplication.run(MySpringBatchApplication.class, args);
  }

  @Override //from CommandLineRunner interface (run automatically)
  public void run(String... args) throws Exception {
	  if(launchRunAfterMain) {
		 initDefaultJobName();
	     runAfterMain(args);
	     //reRunAfterMain(args);
	  }
  }
  
  public void reRunAfterMain(String... args) throws Exception {
	  restartUncompletedJob(this.defaultJobName);
  }
  
  public void initDefaultJobName() {
	 // this.defaultJobName = "myHelloWorldJob";
	  //this.defaultJobName = "myHelloWorldWithParameterJob";
      //this.defaultJobName = "copyFromCsvToCsvJob";
	  //this.defaultJobName = "fromFixedPosTxtToCsvJob";
      //this.defaultJobName = "fromCsvToXmlJob";
	  //this.defaultJobName = "fromCsvToJsonJob";
	  //this.defaultJobName = "fromCsvToFixedPosTxtJob";
	  this.defaultJobName = "fromXmlToCsvJob";
	  //this.defaultJobName = "fromJsonToXmlJob";
      //this.defaultJobName = "insertIntoDbFromCsvJob";
	  //this.defaultJobName = "insertIntoCsvFromDbJob";
	  //this.defaultJobName = "withMyTaskletJob";
	  //this.defaultJobName = "mySimpleSequentialStepsJob";
	  //this.defaultJobName = "mySimpleConditionalStepsJob";
	  //this.defaultJobName = "myPartitionJob";
	  //this.defaultJobName = "generateDbDataSetJob";
	  //this.defaultJobName = "increaseEmployeeSalaryJob";
	  //this.defaultJobName = "fromCsvWithNumAndAddressToJsonJob";
	  // this.defaultJobName = "simpleCounterRestartableJob";
  }
  
  public void runAfterMain(String... args) throws Exception {
	  
	/*
	 NB: if xmlJobConfig profile is activated ,
	 from xml job config ... (job/myHelloWorldJob.xml and SomeJobsFromXmlConfig)  
	 */

	  
	Job job = (Job) applicationContext.getBean(this.defaultJobName);


    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("timeStampOfJobInstance", System.currentTimeMillis())//Necessary for running several instances of a same job (each jobInstance must have a parameter that changes)
        .addString("msg1", "_my_msg1_value_")//used by PrintJobParamMessageTaskletBean and some Reader/Writer
        .addString("enableUpperCase", "true")//used by SimpleUppercasePersonProcessor
        .addLong("dataSetSize", 200L) //used by generateDbDataSetJob
        .toJobParameters();
    
    var jobExecution = jobLauncher.run(job, jobParameters);

    var batchStatus = jobExecution.getStatus();
    while (batchStatus.isRunning()) {
      System.out.println("Still running...");
      Thread.sleep(5000L);
    }
    JobInstance jobInstance = jobExecution.getJobInstance();
    System.out.println("jobInstance.jobName=" + jobInstance.getJobName());
    System.out.println("jobInstance.id=" + jobInstance.getId());
    System.out.println("jobExecution.id=" + jobExecution.getId());
    System.out.println("jobExecution.exitStatus=" + jobExecution.getExitStatus());
  }
  
  public void restartUncompletedJob(String jobName) {
		  JobExecution mostRecentJobExecution = this.findAndShowMostRecentJobExecution(jobName);
		  //JobExecution mostRecentJobExecution = this.findAndShowMostRecentRunningJobExecution(jobName); //return null if no running job
		  
		  if(mostRecentJobExecution!=null)
			  this.restartJobExecution(mostRecentJobExecution);
  }
  
  public void restartJobExecution(JobExecution jobExecution) {
	  try {
		  System.out.println("**** restartJobExecution ****");
		  JobOperator jobOperator= applicationContext.getBean(JobOperator.class);
		  jobOperator.restart(jobExecution.getId());
	  } catch (Exception e) {
	      logger.error(e.getMessage(), e);
	  }
  }
  
  public JobExecution findAndShowMostRecentJobExecution(String jobName) {
	  JobExecution mostRecentJobExecution=null;
	  try {
		  System.out.println("**** findAndShowMostRecentJobExecution ****");
		  JobExplorer jobExplorer = applicationContext.getBean(JobExplorer.class);
		  
		  List<String> jobNames = jobExplorer.getJobNames();
		  System.out.println("jobNames=" + jobNames);
		  long nbInstances = jobExplorer.getJobInstanceCount(jobName);
		  List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName(jobName, 0, (int)nbInstances);
		  System.out.println("jobInstances=" + jobInstances);
		  
		  JobInstance mostRecentJobInstance = jobInstances.get(0);
		  System.out.println("mostRecentJobInstance=" + mostRecentJobInstance);
		  
		  List<JobExecution>  jobExecutions = jobExplorer.getJobExecutions(mostRecentJobInstance);
		  System.out.println("jobExecutions=" + jobExecutions);
		  mostRecentJobExecution = jobExecutions.get(0);
		  System.out.println("mostRecentJobExecution=" + mostRecentJobExecution);
		  System.out.println("executionContext of mostRecentJobExecution=" + mostRecentJobExecution.getExecutionContext());
		  
		  var stepExecutions = mostRecentJobExecution.getStepExecutions();
		  for(StepExecution stepExecution : stepExecutions) {
			  System.out.println("\t stepExecution with exitStatus="+ stepExecution.getExitStatus()
			  +" with status="+ stepExecution.getStatus()+ " and with executionContext=" + stepExecution.getExecutionContext());
		  }
	  } catch (Exception e) {
	      logger.error(e.getMessage(), e);
	  }
	  return  mostRecentJobExecution;
	 }
  
  public JobExecution findAndShowMostRecentRunningJobExecution(String jobName) {
	  JobExecution mostRecentJobExecution=null;
	  try {
		  System.out.println("**** findAndShowMostRecentRunningJobExecution ****");
		  JobExplorer jobExplorer = applicationContext.getBean(JobExplorer.class);
		  Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(jobName);
		  for (JobExecution jobExecution : jobExecutions) {
	      	  if(mostRecentJobExecution==null) mostRecentJobExecution=jobExecution; 
	      	}
		  if(mostRecentJobExecution!=null) { 
		    System.out.println("executionContext of mostRecentJobExecution=" + mostRecentJobExecution.getExecutionContext());
		  
		     var stepExecutions = mostRecentJobExecution.getStepExecutions();
		    for(StepExecution stepExecution : stepExecutions) {
			   System.out.println("\t stepExecution with exitStatus="+ stepExecution.getExitStatus()
			   +" with status="+ stepExecution.getStatus()+ " and with executionContext=" + stepExecution.getExecutionContext());
		    }
		  }
	  } catch (Exception e) {
	      logger.error(e.getMessage(), e);
	  }
	  return  mostRecentJobExecution;
	 }
}
