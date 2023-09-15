package org.mycontrib.mysecurity.chain.config;

import org.mycontrib.mysecurity.area.config.AreaConfig;
import org.mycontrib.mysecurity.area.config.AreasConfig;
import org.mycontrib.mysecurity.common.extension.MySecurityExtension;
import org.mycontrib.mysecurity.common.extension.WithSecurityFilterChainSubConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/*
 @Component
 @Profile("withSecurity")
 public class SpecificWithSecurityMainFilterChainSubConfig implements WithSecurityFilterChainSubConfig {
 ...
 }
 can override this default implementation
 */

public class DefaultWithSecurityMainFilterChainSubConfig implements WithSecurityFilterChainSubConfig {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultWithSecurityMainFilterChainSubConfig.class);
	
	protected AreasConfig areasConfig; //set by WebProtectedAreaConfigurer and .properties
	
	
	public DefaultWithSecurityMainFilterChainSubConfig(AreasConfig areasConfig){
		this.areasConfig=areasConfig;
	}
	
	public static ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry 
        addPermissionsFromAreaConfig(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests,
  		               AreaConfig areaConfig) {
		
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequestsWithPermissions = authorizeRequests;
		
		if(areaConfig.getWhitelist().length>0)
			authorizeRequestsWithPermissions = authorizeRequestsWithPermissions
			.antMatchers(areaConfig.getWhitelist()).permitAll();
		
		if(areaConfig.getBlacklist().length>0)
			authorizeRequestsWithPermissions = authorizeRequestsWithPermissions
			.antMatchers(areaConfig.getBlacklist()).denyAll();
		
		if(areaConfig.getReadonlylist().length>0)
			authorizeRequestsWithPermissions = authorizeRequestsWithPermissions
			.antMatchers(HttpMethod.GET, areaConfig.getReadonlylist()).permitAll();
		
		if(areaConfig.getProtectedlist().length>0)
			authorizeRequestsWithPermissions = authorizeRequestsWithPermissions
			.antMatchers(areaConfig.getProtectedlist()).authenticated();
		
		return authorizeRequestsWithPermissions;
	}
	
	@Override
	public HttpSecurity prepareRestApiFilterChain(HttpSecurity http) throws Exception {
		http =http.authorizeRequests(
			    		 authorizeRequests -> addPermissionsFromAreaConfig(
			    		      authorizeRequests.antMatchers(HttpMethod.POST, MySecurityExtension.DEFAULT_REST_STANDALONE_LOGIN_PATH).permitAll() 
			    		      , areasConfig.getRest())	
			    		   .antMatchers( "/rest/**").authenticated()//by default
			    		 )
					.cors() // enable CORS (avec @CrossOrigin sur class @RestController)
					.and().csrf().disable();
		return http;
	}


	@Override
	public HttpSecurity prepareSpringMvcSiteFilterChain(HttpSecurity http) throws Exception {
		http=http.authorizeRequests(
				    		 authorizeRequests -> addPermissionsFromAreaConfig(
				    		      authorizeRequests.antMatchers( MySecurityExtension.DEFAULT_SITE_FORM_LOGIN_URI).permitAll()
				  		                           .antMatchers( MySecurityExtension.DEFAULT_SITE_FORM_LOGOUT_URI).permitAll() 			  		                   
				    		      ,areasConfig.getSite())
				    		 .antMatchers( "/site/**").authenticated()//by default
				    		 )
					.formLogin().loginPage(MySecurityExtension.DEFAULT_SITE_FORM_LOGIN_URI)
			        .and().csrf()
			        .and().cors().disable();
		return http;
	}


	@Override
	public HttpSecurity prepareDefaultOtherWebPartFilterChain(HttpSecurity http) throws Exception {
		http=http.authorizeRequests(
				    		 authorizeRequests -> addPermissionsFromAreaConfig(
				    				                  addPermissionsFromAreaConfig(authorizeRequests,areasConfig.getTools()),
				    				                  areasConfig.getOther())	
				    		 					.antMatchers( "/**").authenticated()//by default
				    		 )
		        //.headers().frameOptions().disable() //ok for h2-console
		        .headers().frameOptions().sameOrigin() //ok for h2-console
		        .and().csrf().disable();//ok for h2-console
		return http;
	}

}
