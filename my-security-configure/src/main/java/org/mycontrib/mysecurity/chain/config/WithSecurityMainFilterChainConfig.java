package org.mycontrib.mysecurity.chain.config;

import org.mycontrib.mysecurity.chain.properties.MySecurityChainProperties;
import org.mycontrib.mysecurity.common.MyFilterChainSimpleConfigurer;
import org.mycontrib.mysecurity.common.MyRealmConfigurer;
import org.mycontrib.mysecurity.common.RealmPurposeEnum;
import org.mycontrib.mysecurity.common.extension.WithSecurityFilterChainSubConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("withSecurity")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//necessary for @PreAuthorize("hasRole('ADMIN or ...')")
@ConfigurationPropertiesScan("org.mycontrib.mysecurity.chain.properties")
public class WithSecurityMainFilterChainConfig {
	
	private static Logger logger = LoggerFactory.getLogger(WithSecurityMainFilterChainConfig.class);
	
	@Autowired
	private WithSecurityFilterChainSubConfig withSecurityFilterChainSubConfig;

	@Autowired(required = false)
	public MySecurityChainProperties mySecurityChainProperties;
	
	
	@Autowired(required = true)
	@Qualifier("OAuth2ResourceServer")
	protected MyFilterChainSimpleConfigurer myOauth2FilterSimpleConfigurer;
	
	
	@Autowired(required = false)
	@Qualifier("StandaloneJwt")
	protected MyFilterChainSimpleConfigurer myStandaloneJwtFilterSimpleConfigurer;
	
	@Autowired(required = false)
	MyRealmConfigurer myRealmConfigurer;
	
	
	
	//NB: 3 securityChain avec ordre important à respecter
	//@Order(1) pour les URL commencant par /rest (ex: /rest/api-xxx , /rest/api-yyy)
	//@Order(2) pour une éventuelle partie /site/ basée sur @Controller + JSPoyThymeleaf
	//@Order(3) pour le reste (pages static ou pas "spring")
	
	//NB: quand une requête arrive, le FilterChainProxy  de Spring-security
	//va utiliser le premier SecurityFilterChain correpondant à l'url de la requête.
	//et va ignorer les autres (NB: la correspondance se fait via httpSecurity.antMatcher() sans s
	//conventions d'URL : /rest/api-xyz/... ou /site/... ou **
	

	
	@Bean
    @Order(1)
	protected SecurityFilterChain restApiFilterChain(HttpSecurity http)
			throws Exception {
		
		HttpSecurity forSpecificChainHttp =
				http.antMatcher("/rest/**"); //VERY IMPORTANT (matching for rest api and @Order(1) FilterChain)

		http = withSecurityFilterChainSubConfig.prepareRestApiFilterChain(forSpecificChainHttp);
		
		http = withSpecificAuthenticationManagerIfNotNull(http,RealmPurposeEnum.rest);
	
		
		if(withSecurityFilterChainSubConfig.cancelRestConfigPostAction()==false) {
			MyFilterChainSimpleConfigurer myFilterSimpleConfigurer=myOauth2FilterSimpleConfigurer; //by default
			logger.debug("myStandaloneJwtFilterSimpleConfigurer="+myStandaloneJwtFilterSimpleConfigurer);
			logger.info("rest-auth-type="+mySecurityChainProperties.getRestAuthType());
			
			
			
			if(myStandaloneJwtFilterSimpleConfigurer!=null) {
				if(mySecurityChainProperties.getRestAuthType()!=null
						&& mySecurityChainProperties.getRestAuthType().equals("StandaloneJwt"))
				   myFilterSimpleConfigurer=myStandaloneJwtFilterSimpleConfigurer;
			}
			http =  myFilterSimpleConfigurer.configureEndOfSecurityChain(http);
		}

		return http.build();
		
	}
	
	@Bean
    @Order(2)
	protected SecurityFilterChain springMvcSiteFilterChain(HttpSecurity http)
			throws Exception {
		
		/*
		 IMPORTANT DEFAULT VALUE : .and().csrf()
		 tant que pas .and().csrf().disable()
		 et donc besoin de 
		     <input type="hidden"  name="${_csrf.parameterName}"  value="${_csrf.token}"/>
		 ou de
		     <form:form> ou equivalent thymeleaf
		 sinon 403 / Forbidden !!!!
		 */

		HttpSecurity forSpecificChainHttp =
				http.antMatcher("/site/**"); //VERY IMPORTANT (matching for spring mvc site part and @Order(2) FilterChain

		http = withSecurityFilterChainSubConfig.prepareSpringMvcSiteFilterChain(forSpecificChainHttp);
	
		
		http = withSpecificAuthenticationManagerIfNotNull(http,RealmPurposeEnum.site);
		return http.build();
		
	}
	
	/*
	 * .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/site/logout", "GET"))
	 * ok mais redirection automatique vers login?logout
	 */
	
	@Bean
    @Order(3)
	protected SecurityFilterChain staticWebPartFilterChain(HttpSecurity http)
			throws Exception {
		
		HttpSecurity forSpecificChainHttp =
				http.antMatcher("/**"); //VERY IMPORTANT (matching for all other parts (static, ...) and @Order(3) FilterChain)

		http = withSecurityFilterChainSubConfig.prepareDefaultOtherWebPartFilterChain(forSpecificChainHttp);
        
		return http.build();
			
	}
	
	private AuthenticationManager getDefaultOrSpecificRealmAuthenticationManager(HttpSecurity httpSecurity,
			RealmPurposeEnum realmPurpose){
		AuthenticationManager specificAuthMgr = withSecurityFilterChainSubConfig.provideSpecificRealmAuthenticationManager(httpSecurity, realmPurpose);
		if(specificAuthMgr!=null)
			return specificAuthMgr;
		if(myRealmConfigurer==null) return null;
		return myRealmConfigurer.getRealmAuthenticationManager(httpSecurity,realmPurpose);
	}
	
	
	
	@Bean //default globalAuthenticationManager
	//@ConditionalOnMissingBean(AuthenticationManager.class)
	@Qualifier("global")
	@Primary
	public AuthenticationManager globalAuthenticationManager(HttpSecurity httpSecurity)throws Exception {
		return getDefaultOrSpecificRealmAuthenticationManager(httpSecurity,RealmPurposeEnum.global);
	}
	
	@Bean //default restAuthenticationManager
	//@ConditionalOnMissingBean(AuthenticationManager.class)
	@Qualifier("rest")
	public AuthenticationManager restAuthenticationManager(HttpSecurity httpSecurity)throws Exception {
		return getDefaultOrSpecificRealmAuthenticationManager(httpSecurity,RealmPurposeEnum.rest);
	}
	
	@Bean //default siteAuthenticationManager
	//@ConditionalOnMissingBean(AuthenticationManager.class)
	@Qualifier("site")
	public AuthenticationManager siteAuthenticationManager(HttpSecurity httpSecurity)throws Exception {
		return getDefaultOrSpecificRealmAuthenticationManager(httpSecurity,RealmPurposeEnum.site);
	}
	
	private HttpSecurity withSpecificAuthenticationManagerIfNotNull(HttpSecurity http,RealmPurposeEnum realmPurpose)throws Exception {
	    AuthenticationManager authMgr =  getDefaultOrSpecificRealmAuthenticationManager(http,realmPurpose);
	    if(authMgr!=null)
	    	return http.authenticationManager(authMgr);
	    else
	    	return http;
    }

}
