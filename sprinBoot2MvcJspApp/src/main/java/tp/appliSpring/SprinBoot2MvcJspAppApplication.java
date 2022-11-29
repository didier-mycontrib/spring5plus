package tp.appliSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SprinBoot2MvcJspAppApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SprinBoot2MvcJspAppApplication.class, args);
		SpringApplication app = new SpringApplication(SprinBoot2MvcJspAppApplication.class);
		app.setAdditionalProfiles("initDataSet"); // "dev" , "logPerf"
		//app.setAdditionalProfiles("dev","withSecurity");
		//app.setAdditionalProfiles("prod");
		ConfigurableApplicationContext context = app.run(args);
		//context.getBean("...")
		System.out.println("http://localhost:8080/appliSpringBoot");
	}

}
