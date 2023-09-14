package org.mycontrib.mysecurity.area.config;

import java.util.Arrays;
import java.util.stream.Stream;

import org.mycontrib.mysecurity.area.properties.MySecurityAreaProperties;
import org.mycontrib.mysecurity.area.properties.MySecurityAreasProperties;
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
		loadAreaConfigFromAreaProperties(areasConfig.getSite(),mySecurityProperties.getRest());
		loadAreaConfigFromAreaProperties(areasConfig.getOther(),mySecurityProperties.getOther());
		loadAreaConfigFromAreaProperties(areasConfig.getTools(),mySecurityProperties.getTools());
	}

	@Bean 
	protected AreasConfig areasConfig() {
		
		AreasConfig areasConfig = new AreasConfig();
		loadAreasConfigFromAreasProperties(areasConfig);//load areas configs from .properties

		String[] defaultStaticWhitelist = { "/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg",
				"/**/*.html", "/**/*.css", "/**/*.js" }; // default value
		
		String[] defaultApiWhitelist = { "/rest/my-api/public/**" }; // default value
		
		String[] defaultToolsWhitelist = { "/swagger-ui/**","/v3/api-docs" ,"/h2-console/**"}; 
		
		String[] defaultApiReadonlyWhitelist = { "/rest/my-api/readonly/**" }; // default value
		
		String[] defaultApiProtectedlist = { "/rest/my-api/private/**" }; // default value
		
		if(areasConfig.getOther().getWhitelist().length==0)
			areasConfig.getOther().setWhitelist(defaultStaticWhitelist);
		
		if(areasConfig.getTools().getWhitelist().length==0)
			areasConfig.getTools().setWhitelist(defaultToolsWhitelist);
		
		if(areasConfig.getRest().getWhitelist().length==0)
			areasConfig.getRest().setWhitelist(defaultApiWhitelist);
		
		if(areasConfig.getRest().getReadonlylist().length==0)
			areasConfig.getRest().setReadonlylist(defaultApiReadonlyWhitelist);
		
		if(areasConfig.getRest().getProtectedlist().length==0)
			areasConfig.getRest().setProtectedlist(defaultApiProtectedlist);
	
		
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