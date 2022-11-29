package tp.appliSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBoot2MapStructApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SpringBoot2MapStructApplication.class, args);
		SpringApplication app = new SpringApplication(SpringBoot2MapStructApplication.class);
		app.setAdditionalProfiles("dev");
		//app.setAdditionalProfiles("prod");
		ConfigurableApplicationContext context = app.run(args);
		System.out.println("http://localhost:8080/appliSpringBoot2WithMapStruct");
	}

}
