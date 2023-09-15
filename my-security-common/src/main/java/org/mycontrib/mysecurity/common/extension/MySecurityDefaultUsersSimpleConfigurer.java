package org.mycontrib.mysecurity.common.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface MySecurityDefaultUsersSimpleConfigurer {
	default void configureRestDefaultUsers(UserDetailsManagerConfigurer udmc) {
		configureGlobalDefaultUsers(udmc);
	}
	default void configureSiteDefaultUsers(UserDetailsManagerConfigurer udmc) {
		configureGlobalDefaultUsers(udmc);
	}
	void configureGlobalDefaultUsers(UserDetailsManagerConfigurer udmc);
	//...
}


/*
//NB: on peut créer une implémentation spécifique de cette interface
//dans le projet principal qui sera alors prioritaire par rapport à l'implémentation par défaut
//exemple :
@Component @Profile("withSecurity")
public class MySecurityDefaultUsersSimpleConfigurerSpecificImpl implements MySecurityDefaultUsersSimpleConfigurer {

	
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public MySecurityDefaultUsersSimpleConfigurerDefaultImpl(BCryptPasswordEncoder passwordEncoder){
		this.passwordEncoder=passwordEncoder;
	}
	
	@Override
	public void configureGlobalDefaultUsers(UserDetailsManagerConfigurer udmc) {
		udmc
		.withUser("user1").password(passwordEncoder.encode("pwd1")).roles("USER")
		.and().withUser("admin1").password(passwordEncoder.encode("pwd1")).roles("ADMIN")
		.and().withUser("user2").password(passwordEncoder.encode("pwd2")).roles("USER")
		.and().withUser("admin2").password(passwordEncoder.encode("pwd2")).roles("ADMIN");
	}

}

*/