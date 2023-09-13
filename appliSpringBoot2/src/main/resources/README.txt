variantes de configurations (profils):
-------------------------------------

"dev" ou "prod"
...


Variantes de sécurité:
======================

Sécurité par défaut :
---------------------
si spring-boot-starter-security dans pom.xml
et aucune autre configuration 
alors au démarrage de l'appli springBoot dans la console:

Using generated security password: 188fcfe1-7b8d-44fb-ae29-959fb9362170
This generated password is for development use only. Your security configuration must be updated before running your application in production.

Et ainsi dans le formulaire de login présenté automatiquement à l'accès de index.html
il faut saisir "user" comme username
et copier/coller le mot de passe généré aléatoirement au démarrage et affiché dans la console.

NB: cette sécurité par défaut convient à peu près pour @Controller + thymeleaf_ou_jsp
mais n'est pas bien adpatée au api-rest nécessitant généralement une authentification à base de token.

my-security
===========

si my-security-starter dans pom.xml
alors 
  - par défaut (si aucune autre configuration) , aucune sécurité (tout en permitAll en début de dev)
  - securité activée que si activation du profile "withSecurity"

* si "withSecurity" et aucune autre configuration alors les valeurs par défaut seront les suivantes:
En mode "inMemory" :
public void configureDefaultUsers(UserDetailsManagerConfigurer udmc) {
		udmc
		.withUser("user1").password(passwordEncoder.encode("pwd1")).roles("USER")
		.and().withUser("admin1").password(passwordEncoder.encode("pwd1")).roles("ADMIN")
		.and().withUser("user2").password(passwordEncoder.encode("pwd2")).roles("USER")
		.and().withUser("admin2").password(passwordEncoder.encode("pwd2")).roles("ADMIN");
	}
dans org.mycontrib.mysecurity.config.MySecuritySimpleConfigurerDefaultImpl du projet my-security-configure
Et avec:
String[] staticWhitelist = { "/", "/h2-console/**/*.*" , "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg",
				"/**/*.html", "/**/*.css", "/**/*.js" }; // default value
String[] apiWhitelist = { "/my-api/public/**" }; // default value
String[] apiReadonlyWhitelist = { "/my-api/readonly/**" }; // default value
String[] apiProtectedlist = { "/my-api/private/**" }; // default value

   