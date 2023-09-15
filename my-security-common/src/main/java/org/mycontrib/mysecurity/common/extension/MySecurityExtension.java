package org.mycontrib.mysecurity.common.extension;

public interface MySecurityExtension {
	//end of URL for predefined REST login WS (to call via method=POST)
	//to get JWT token in standalone mode (no Oauth2/Oidc)
	public static final String DEFAULT_REST_STANDALONE_LOGIN_PATH="/rest/api-login/public/login";
	
	//URI of login form (not predefined, may be define in final project if necessary):
	public static final String DEFAULT_SITE_FORM_LOGIN_URI="/site/login";
	
	//URI of logout form (not predefined, may be define in final project if necessary):
	public static final String DEFAULT_SITE_FORM_LOGOUT_URI="/site/logout";
	
	//default value for mysecurity.area.other.whitelist
	public static final String[] DEFAULT_STATIC_WHITELIST = { "/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg",
			"/**/*.html", "/**/*.css", "/**/*.js" }; 
	
	//partie de l'url (au debut) devant mener à une api REST (ex: /rest/api-xyz/xyz)
	public static final String REST_PART_URI="/rest";
	
	//partie de l'url (au debut) devant mener à une partie du site web (@Controller + JSP ou Thymeleaf)  (ex: /site/xxx/zzz)
	public static final String SITE_PART_URI="/site";
	
	//constantes pour nom à donner au composant de UserDetailsService spécifique au projet
	//EXCLUSIVE si celui ci remplace la config par défaut de ce projet ("InMemory" ou autre)
	//ADDITIONAL si celui s'ajoute à la config par défaut de ce projet
	
	//Si le UserDetailsService peut aussi bien être utilisé sur partie "site" que partie "rest"
	public static final String MY_EXCLUSIVE_USERDETAILSSERVICE_NAME="MyExclusiveUserDetailsService";
	public static final String MY_ADDITIONAL_USERDETAILSSERVICE_NAME="MyAdditionalUserDetailsService";
	
	//Si le UserDetailsService ne doit être utilisé sur partie "rest"
	public static final String MY_EXCLUSIVE_RESTONLY_USERDETAILSSERVICE_NAME="MyExclusiveRestOnlyUserDetailsService";
	public static final String MY_ADDITIONAL_RESTONLY_USERDETAILSSERVICE_NAME="MyAdditionalRestOnlyUserDetailsService";
	
	//Si le UserDetailsService ne doit être utilisé sur partie "site"
	public static final String MY_EXCLUSIVE_SITEONLY_USERDETAILSSERVICE_NAME="MyExclusiveSiteOnlyUserDetailsService";
	public static final String MY_ADDITIONAL_SITEONLY_USERDETAILSSERVICE_NAME="MyAdditionalSiteOnlyUserDetailsService";
}

/*
 EXEMPLE DE USERDETAILSSERVICE (specifique a un projet):
 
 @Profile("withSecurity")
//@Service(MySecurityExtension.MY_EXCLUSIVE_USERDETAILSSERVICE_NAME)
@Service(MySecurityExtension.MY_ADDITIONAL_USERDETAILSSERVICE_NAME)
public class MyUserDetailsService implements UserDetailsService {
	
	Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
	
	
	@Autowired
    private PasswordEncoder passwordEncoder;
   
	
	@Autowired
	private ServiceCustomer serviceCustomer;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails=null;
		logger.debug("MyUserDetailsService.loadUserByUsername() called with username="+username);
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String password=null;
		if(username.equals("james_Bond")) {
			password=passwordEncoder.encode("007");//simulation password ici
			authorities.add(new SimpleGrantedAuthority("ROLE_AGENTSECRET"));
			userDetails = new User(username, password, authorities);
		}
		else {
			//NB le username considéré comme potentiellement
			//égal à firstname_lastname
			try {
				String firstname = username.split("_")[0];
				String lastname = username.split("_")[1];
					
				List<Customer> customers = serviceCustomer.rechercherCustomerSelonPrenomEtNom(firstname,lastname);
				if(!customers.isEmpty()) {
					Customer firstCustomer = customers.get(0);
					authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));//ou "ROLE_USER" ou "ROLE_ADMIN"
					password=firstCustomer.getPassword();// déjà stocké en base en mode crypté
					//password=passwordEncoder.encode(firstCustomer.getPassword());//si pas stocké en base en mode crypté (PAS BIEN !!!)
					userDetails = new User(username, password, authorities);
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		
		
		if(userDetails==null) {
			//NB: il est important de remonter UsernameNotFoundException (mais pas null , ni une autre exception)
			//si l'on souhaite qu'en cas d'échec avec cet AuthenticationManager
			//un éventuel AuthenticationManager parent soit utilisé en plan B
			throw new UsernameNotFoundException(username + " not found by MyUserDetailsService");
		}
			
		return userDetails;
	} 

}

 */
