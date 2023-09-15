package org.mycontrib.appliSpringWeb;

import org.mycontrib.mysecurity.common.extension.MySecurityDefaultUsersSimpleConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//@Component //NB: this extension in currently desactivated !!!
@Profile("withSecurity")
public class MySecurityDefaultUsersSimpleConfigurerSpecificImpl implements MySecurityDefaultUsersSimpleConfigurer {

private PasswordEncoder passwordEncoder;
	
	@Autowired
	public MySecurityDefaultUsersSimpleConfigurerSpecificImpl(PasswordEncoder passwordEncoder){
		this.passwordEncoder=passwordEncoder;
	}
	
	@Override
	public void configureGlobalDefaultUsers(UserDetailsManagerConfigurer udmc) {
		udmc
		//same as default implementation:
		.withUser("user1").password(passwordEncoder.encode("pwd1")).roles("USER")
		.and().withUser("admin1").password(passwordEncoder.encode("pwd1")).roles("ADMIN")
		.and().withUser("user2").password(passwordEncoder.encode("pwd2")).roles("USER")
		.and().withUser("admin2").password(passwordEncoder.encode("pwd2")).roles("ADMIN")
		//specific others users:
		.and().withUser("user3").password(passwordEncoder.encode("pwd3")).roles("USER")
		.and().withUser("admin3").password(passwordEncoder.encode("pwd3")).roles("ADMIN");
	}
	
	//Effet immediat en mode InMemory
	//attention, en mode jdbc , la base de données "realmdb..." doit être (manuellement) réinitialisée
	
}
