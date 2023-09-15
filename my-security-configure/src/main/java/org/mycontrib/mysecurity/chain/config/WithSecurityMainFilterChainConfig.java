package org.mycontrib.mysecurity.chain.config;

import org.mycontrib.mysecurity.area.config.AreaConfig;
import org.mycontrib.mysecurity.area.config.AreasConfig;
import org.mycontrib.mysecurity.chain.properties.MySecurityChainProperties;
import org.mycontrib.mysecurity.common.MyFilterChainSimpleConfigurer;
import org.mycontrib.mysecurity.common.MyRealmConfigurer;
import org.mycontrib.mysecurity.common.RealmPurposeEnum;
import org.mycontrib.mysecurity.common.extension.MySecurityExtension;
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
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("withSecurity")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//necessary for @PreAuthorize("hasRole('ADMIN or ...')")
@ConfigurationPropertiesScan("org.mycontrib.mysecurity.chain.properties")
public class WithSecurityMainFilterChainConfig {
	
	private static Logger logger = LoggerFactory.getLogger(WithSecurityMainFilterChainConfig.class);
	
	

	@Autowired(required = false)
	public MySecurityChainProperties mySecurityChainProperties;
	
	@Autowired
	protected AreasConfig areasConfig; //set by WebProtectedAreaConfigurer and .properties
	
	@Autowired(required = true)
	@Qualifier("OAuth2ResourceServer")
	protected MyFilterChainSimpleConfigurer myOauth2FilterSimpleConfigurer;
	
	
	@Autowired(required = false)
	@Qualifier("StandaloneJwt")
	protected MyFilterChainSimpleConfigurer myStandaloneJwtFilterSimpleConfigurer;
	
	@Autowired(required = false)
	MyRealmConfigurer myRealmConfigurer;
	
	/*
	 public static AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry 
        addPermissions(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequests,
  		               AreaConfig areaConfig) {
	 */
	
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

		HttpSecurity partialConfiguredHttp =
		     http.antMatcher("/rest/**") //VERY IMPORTANT (matching for rest api and @Order(1) FilterChain)
		     .authorizeRequests(
		    		 authorizeRequests -> addPermissionsFromAreaConfig(
		    		      authorizeRequests.antMatchers(HttpMethod.POST, MySecurityExtension.DEFAULT_REST_STANDALONE_LOGIN_PATH).permitAll() 
		    		      , areasConfig.getRest())	
		    		   .antMatchers( "/rest/**").authenticated()//by default
		    		 )
				.cors() // enable CORS (avec @CrossOrigin sur class @RestController)
				.and().csrf().disable();

		partialConfiguredHttp = withSpecificAuthenticationManagerIfNotNull(partialConfiguredHttp,RealmPurposeEnum.rest);
	
		
		MyFilterChainSimpleConfigurer myFilterSimpleConfigurer=myOauth2FilterSimpleConfigurer; //by default
		logger.debug("myStandaloneJwtFilterSimpleConfigurer="+myStandaloneJwtFilterSimpleConfigurer);
		logger.info("rest-auth-type="+mySecurityChainProperties.getRestAuthType());
		
		
		
		if(myStandaloneJwtFilterSimpleConfigurer!=null) {
			if(mySecurityChainProperties.getRestAuthType()!=null
					&& mySecurityChainProperties.getRestAuthType().equals("StandaloneJwt"))
			   myFilterSimpleConfigurer=myStandaloneJwtFilterSimpleConfigurer;
		}

		HttpSecurity fullConfiguredHttp =  myFilterSimpleConfigurer.configureEndOfSecurityChain(partialConfiguredHttp);
		return fullConfiguredHttp.build();
		
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

	
		http=http
		      .antMatcher("/site/**") //VERY IMPORTANT (matching for spring mvc site part and @Order(2) FilterChain
		      .authorizeRequests(
			    		 authorizeRequests -> addPermissionsFromAreaConfig(
			    		      authorizeRequests.antMatchers( MySecurityExtension.DEFAULT_SITE_FORM_LOGIN_URI).permitAll()
			  		                           .antMatchers( MySecurityExtension.DEFAULT_SITE_FORM_LOGOUT_URI).permitAll() 			  		                   
			    		      ,areasConfig.getSite())
			    		 .antMatchers( "/site/**").authenticated()//by default
			    		 )
				.formLogin().loginPage(MySecurityExtension.DEFAULT_SITE_FORM_LOGIN_URI)
		        .and().csrf()
		        .and().cors().disable();
		
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

		http=http.antMatcher("/**") //VERY IMPORTANT (matching for all other parts (static, ...) and @Order(3) FilterChain)
				  .authorizeRequests(
				    		 authorizeRequests -> addPermissionsFromAreaConfig(
				    				                  addPermissionsFromAreaConfig(authorizeRequests,areasConfig.getTools()),
				    				                  areasConfig.getOther())	
				    		 					.antMatchers( "/**").authenticated()//by default
				    		 )
		        //.headers().frameOptions().disable() //ok for h2-console
		        .headers().frameOptions().sameOrigin() //ok for h2-console
		        .and().csrf().disable();//ok for h2-console
		return http.build();
			
	}
	
	@Bean //default globalAuthenticationManager
	//@ConditionalOnMissingBean(AuthenticationManager.class)
	@Qualifier("global")
	@Primary
	public AuthenticationManager globalAuthenticationManager(HttpSecurity httpSecurity)throws Exception {
		    if(myRealmConfigurer==null) return null;
			return myRealmConfigurer.getRealmAuthenticationManager(httpSecurity,RealmPurposeEnum.global);
	}
	
	@Bean //default restAuthenticationManager
	//@ConditionalOnMissingBean(AuthenticationManager.class)
	@Qualifier("rest")
	public AuthenticationManager restAuthenticationManager(HttpSecurity httpSecurity)throws Exception {
		    if(myRealmConfigurer==null) return null;
			return myRealmConfigurer.getRealmAuthenticationManager(httpSecurity,RealmPurposeEnum.rest);
	}
	
	@Bean //default siteAuthenticationManager
	//@ConditionalOnMissingBean(AuthenticationManager.class)
	@Qualifier("site")
	public AuthenticationManager siteAuthenticationManager(HttpSecurity httpSecurity)throws Exception {
		    if(myRealmConfigurer==null) return null;
			return myRealmConfigurer.getRealmAuthenticationManager(httpSecurity,RealmPurposeEnum.site);
	}
	
	private HttpSecurity withSpecificAuthenticationManagerIfNotNull(HttpSecurity http,RealmPurposeEnum realmPurpose)throws Exception {
	    if(myRealmConfigurer==null) return http;
	    AuthenticationManager authMgr =  myRealmConfigurer.getRealmAuthenticationManager(http,realmPurpose);
	    if(authMgr!=null)
	    	return http.authenticationManager(authMgr);
	    else
	    	return http;
    }

}
