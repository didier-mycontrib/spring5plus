package tp.appliSpring.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	
	MyWebApplicationInitializer(){
		  System.out.println("MyWebApplicationInitializer  ...");
	}
	
	protected String[] getServletMappings() {
        return new String[]{"/mvc/*"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{MyWebAppConfig.class};
    }

    
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }
    
    @Override
    public void onStartup(ServletContext context) throws ServletException {
      super.onStartup(context);

      //String activeProfile = "";
      String activeProfile = "initDataSet";

      context.setInitParameter("spring.profiles.active", activeProfile);
    }
    
}

//older configuration with little bug ....
/*
public class MyWebApplicationInitializer implements WebApplicationInitializer  {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//enregistrement en mode java config des composants "web" de spring
		//remplace WEB-INF/web.xml avec servlet, servlet-mapping et listerner web
		AnnotationConfigWebApplicationContext webApplicationContext = new	AnnotationConfigWebApplicationContext ();
		webApplicationContext.register (MyWebAppConfig.class );
		servletContext.addListener(new ContextLoaderListener (webApplicationContext ));
		
		var servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(webApplicationContext));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/mvc/*");
        System.out.println("WebApplicationInitializer / onStartup ...");
	}
	
}
*/
