package tp.appliSpring.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller 
@RequestMapping("/site/other")
public class OtherCtrl {
	
	@RequestMapping("/hello-other")
	@ResponseBody //si @ResponseBody , génération directe de la réponse
	public String versLogin(Model model) {
	    return "Hello other !";
	}
	
}
