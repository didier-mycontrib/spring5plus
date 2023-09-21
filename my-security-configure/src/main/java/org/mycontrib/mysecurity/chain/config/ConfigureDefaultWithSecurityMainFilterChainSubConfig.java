package org.mycontrib.mysecurity.chain.config;

import org.mycontrib.mysecurity.area.config.AreasConfig;
import org.mycontrib.mysecurity.common.extension.WithSecurityFilterChainSubConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("withSecurity")
@ConfigurationPropertiesScan("org.mycontrib.mysecurity.chain.properties")
public class ConfigureDefaultWithSecurityMainFilterChainSubConfig  {
	

	@Autowired
	protected AreasConfig areasConfig; //set by WebProtectedAreaConfigurer and .properties
	
	
	@Bean
	@ConditionalOnMissingBean(type= { "org.mycontrib.mysecurity.common.extension.WithSecurityFilterChainSubConfig" })
	public WithSecurityFilterChainSubConfig  DefaultWithSecurityMainFilterChainSubConfig(PasswordEncoder passwordEncoder) {
		return new DefaultWithSecurityMainFilterChainSubConfig(areasConfig);
	}
	
	
}
