package tp.appliSpring.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller 
public class LoginCtrl {
	
	//dans index.html <a href="site/login"> .... </a>
	@RequestMapping("/site/login")
	public String versLogin(Model model) {
		return "login"; // .../jsp/login.jsp
	}
		
	@RequestMapping("/site/logout")
	public String doLogout(Model model) {
		SecurityContextHolder.getContext().setAuthentication(null);
		return "logout"; // .../jsp/logout.jsp
	}
}
