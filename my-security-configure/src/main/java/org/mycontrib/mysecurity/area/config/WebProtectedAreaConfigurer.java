package org.mycontrib.mysecurity.area.config;

import java.util.Arrays;
import java.util.stream.Stream;

import org.mycontrib.mysecurity.area.properties.MySecurityAreaProperties;
import org.mycontrib.mysecurity.area.properties.MySecurityAreasProperties;
import org.mycontrib.mysecurity.common.extension.MySecurityExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("withSecurity")
@ConfigurationPropertiesScan("org.mycontrib.mysecurity.area.properties")
public class WebProtectedAreaConfigurer {

	private static Logger logger = LoggerFactory.getLogger(WebProtectedAreaConfigurer.class);

	@Autowired(required = false)
	public MySecurityAreasProperties mySecurityProperties;

	public static String[] concatenateArray(String[] first, String[] second)
	{
	    return Stream.concat(Arrays.stream(first), Arrays.stream(second))
	                    .toArray(String[]::new);
	}
	
    private void loadAreaConfigFromAreaProperties(AreaConfig areaConfig,MySecurityAreaProperties areaProps) {
    	if (areaProps != null && areaProps.getWhitelist() != null)
			areaConfig.setWhitelist(areaProps.getWhitelist().split(";"));
    	
    	if (areaProps != null && areaProps.getBlacklist() != null)
			areaConfig.setBlacklist(areaProps.getBlacklist().split(";"));
    	
    	if (areaProps != null && areaProps.getReadonlylist() != null)
			areaConfig.setReadonlylist(areaProps.getReadonlylist().split(";"));
    	
    	if (areaProps != null && areaProps.getProtectedlist() != null)
			areaConfig.setProtectedlist(areaProps.getProtectedlist().split(";"));
	}
	
	private void loadAreasConfigFromAreasProperties(AreasConfig areasConfig) {
		if(mySecurityProperties == null) return;
		loadAreaConfigFromAreaProperties(areasConfig.getRest(),mySecurityProperties.getRest());
		loadAreaConfigFromAreaProperties(areasConfig.getSite(),mySecurityProperties.getSite());
		loadAreaConfigFromAreaProperties(areasConfig.getOther(),mySecurityProperties.getOther());
		loadAreaConfigFromAreaProperties(areasConfig.getTools(),mySecurityProperties.getTools());
	}

	@Bean 
	protected AreasConfig areasConfig() {
		
		AreasConfig areasConfig = new AreasConfig();
		loadAreasConfigFromAreasProperties(areasConfig);//load areas configs from .properties
		
		//String[] exempleApiWhitelist = { "/rest/my-api/public/**" }; // default value
		//String[] exempleToolsWhitelist = { "/swagger-ui/**","/v3/api-docs" ,"/h2-console/**"}; 
		//String[] exempleApiReadonlyWhitelist = { "/rest/my-api/readonly/**" }; // default value
		//String[] exampleApiProtectedlist = { "/rest/my-api/private/**" }; // default value
		
		if(areasConfig.getOther().getWhitelist().length==0)
			areasConfig.getOther().setWhitelist(MySecurityExtension.DEFAULT_STATIC_WHITELIST);
		
		//Readaptation de ApiProtectedlist :
		//toute url de ApiReadonlyWhitelist doit normalement être également placée
		//dans ApiProtectedlist pour les appels autres qu'en GET (POST,PUT,DELETE,...)
		areasConfig.getRest().setProtectedlist(this.concatenateArray(areasConfig.getRest().getReadonlylist(),
				                                                     areasConfig.getRest().getProtectedlist()));
		areasConfig.getSite().setProtectedlist(this.concatenateArray(areasConfig.getSite().getReadonlylist(),
                                                                     areasConfig.getSite().getProtectedlist()));

		logger.info("areasConfig=" +areasConfig.toString());
		
		return areasConfig;
	}
}