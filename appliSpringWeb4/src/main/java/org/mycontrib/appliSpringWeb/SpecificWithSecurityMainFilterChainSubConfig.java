package org.mycontrib.appliSpringWeb;

import org.mycontrib.mysecurity.common.extension.WithSecurityFilterChainSubConfig;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

//@Component //NB: this extension in currently desactivated !!!
@Profile("withSecurity")
public class SpecificWithSecurityMainFilterChainSubConfig implements WithSecurityFilterChainSubConfig {
	
	//NB: en phase de test , penser éventuellement à supprimer le cookie JSESSIONID et/ou le "authToken" dans sessionStorage du navigateur

	@Override
	public HttpSecurity prepareDefaultOtherWebPartFilterChain(HttpSecurity http) throws Exception {
		http=http.authorizeRequests(
	    		 authorizeRequests -> authorizeRequests
	    		                    .antMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg",
	    		                			"/**/*.html", "/**/*.css", "/**/*.js").permitAll()
	    		 					.antMatchers( "/**").authenticated()//by default
	    		 )
				.csrf().disable();
		return http;
	}
	
	/*
	
	//METHODES with DEFAULT implementations:

	public  HttpSecurity prepareRestApiFilterChain(HttpSecurity http) throws Exception { 
		http.authorizeRequests().antMatchers( "/rest/**").authenticated(); 
		return http;
	}
	
	
	public  HttpSecurity prepareSpringMvcSiteFilterChain(HttpSecurity http) throws Exception { 
		http.authorizeRequests().antMatchers( "/site/**").authenticated(); 
		return http; 
	}
	
	//NB: after prepare...FilterChain() , some post actions and .build();
	//will be automatically called to build and expose SecurityFilterChain
	
	//post actions for rest and site parts (if exists):
	//attach default or specific AuthenticationManager 
	
	//post action for rest part (if exists):
	//specific oauth2 config (or standalone jwt config) 
	//mysecurity.chain.rest-auth-type=OAuth2ResourceServer(by default) or StandaloneJwt
	//this post action may be cancel if the following method returns true
	
	public boolean cancelRestConfigPostAction() { return false; }
	
	//if returning not null , it will be automatically attached to http (HttpSecurity)
	//of corresponding SecurityChain (rest only, site only, global for rest and site parts)
	public AuthenticationManager provideSpecificRealmAuthenticationManager(HttpSecurity http,RealmPurposeEnum realmPurpose) {
			return null;
	}
	
	*/

}
