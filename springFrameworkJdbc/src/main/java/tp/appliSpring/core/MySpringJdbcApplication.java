package tp.appliSpring.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import tp.appliSpring.core.entity.Compte;
import tp.appliSpring.core.service.ServiceCompte;

//version sans springBoot
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = { "tp.appliSpring.core" , "tp.appliSpring.aspect"})
public class MySpringJdbcApplication {

	public static void main(String[] args) {
		
		//System.setProperty("spring.profiles.active", "perf");
		
		
		/*ApplicationContext*/ AnnotationConfigApplicationContext springContext = new
				AnnotationConfigApplicationContext(MySpringJdbcApplication.class) ;

				
		ServiceCompte serviceCompte = springContext.getBean(ServiceCompte.class);
		Compte cptA = new Compte(null,"compteA",100.0);
		Compte cptA_sauvegarde = serviceCompte.sauvegarderCompte(cptA);
		
		Compte cptA_relu = serviceCompte.rechercherCompteParNumero(cptA_sauvegarde.getNumero());
		System.out.println("cptA_relu="+cptA_relu);
		
		springContext.close();
	}

}
