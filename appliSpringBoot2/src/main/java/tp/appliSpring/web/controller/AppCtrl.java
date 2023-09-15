package tp.appliSpring.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 

@RequestMapping("/site/app")
public class AppCtrl {
	
	@ModelAttribute("idSession")
	public String idSession(HttpSession session) {
		return session.getId();
	}
	
	@RequestMapping("/hello-world-1")
	@ResponseBody //si @ResponseBody , génération directe de la réponse
	String say_hello() {
	    return "Hello World!";
	}
	
	@RequestMapping("/hello-world-2")
	 public String helloWorld(Model model) {
	    model.addAttribute("message", "Hello World!");
	    return "showMessage"; //aiguiller sur la vue "showMessage"
	 }
	
	@RequestMapping("/to-welcome")
	String toWelcome(Model model) {
		model.addAttribute("message", "bienvenu(e)");
		model.addAttribute("title","welcome");
	    return "welcome"; //welcome.jsp ou ... dans src/main/resources/META-INF/resources/jsp ou ailleurs
	}
	
	@RequestMapping("/to-login")
	String toLogin(Model model) {
		System.out.println("/site/app/to-login");
		model.addAttribute("title","login");
	    return "login_client_bank"; //login_client_bank.jsp ou ... dans src/main/resources/META-INF/resources/jsp ou ailleurs
	}
	
	@RequestMapping("/session-end")
    public String finSession(Model model,HttpSession session) {
	    //SecurityContextHolder.clearContext(); //RAZ context Spring security
	    session.invalidate();
        model.addAttribute("message", "session terminée");
        model.addAttribute("title","welcome");
        return "welcome"; 
    }
}
