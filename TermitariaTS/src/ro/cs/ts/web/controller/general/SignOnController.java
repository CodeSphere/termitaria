package ro.cs.ts.web.controller.general;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ro.cs.ts.web.security.UserAuth;

@Controller
public class SignOnController {
	private Logger logger = Logger.getLogger(SignOnController.class);
	
	@RequestMapping("/SignOn.htm")
	public String showLogin(HttpServletRequest request){
		logger.debug("\t showLogin"); 

		return "SignOn";
	}
}
