package org.mycontrib.mysecurity.common.extension;

import org.mycontrib.mysecurity.common.RealmPurposeEnum;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface WithSecurityFilterChainSubConfig {
	
	//if returning not null , it will be automatically attached to http (HttpSecurity)
	//of corresponding SecurityChain (rest only, site only, global for rest and site parts)
	default AuthenticationManager provideSpecificRealmAuthenticationManager(HttpSecurity http,RealmPurposeEnum realmPurpose) {
		return null;
	}
	
	//rest/** @Order(1)
	default  HttpSecurity prepareRestApiFilterChain(HttpSecurity http) throws Exception { 
		http.authorizeRequests().antMatchers( "/rest/**").authenticated(); 
		return http;
	}
	
	//site/** @Order(2)
	default  HttpSecurity prepareSpringMvcSiteFilterChain(HttpSecurity http) throws Exception { 
		http.authorizeRequests().antMatchers( "/site/**").authenticated(); 
		return http; 
	}
	
	//** others @Order(3)
	public HttpSecurity prepareDefaultOtherWebPartFilterChain(HttpSecurity http) throws Exception ;
	
	//NB: after prepare...FilterChain() , some post actions and .build();
	//will be automatically called to build and expose SecurityFilterChain
	
	//post actions for rest and site parts (if exists):
	//attach default or specific AuthenticationManager 
	
	//post action for rest part (if exists):
	//specific oauth2 config (or standalone jwt config) 
	//mysecurity.chain.rest-auth-type=OAuth2ResourceServer(by default) or StandaloneJwt
	//this post action may be cancel if the following method returns true
	
	default boolean cancelRestConfigPostAction() { return false; }
	


}
