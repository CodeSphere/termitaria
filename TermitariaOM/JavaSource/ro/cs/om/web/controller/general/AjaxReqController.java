package ro.cs.om.web.controller.general;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.cs.om.utils.security.SecurityTokenManager;

@Controller
public class AjaxReqController {
//	private static String SECURITY_TOKEN = "wjdk93ls2093swiks724544mdmchfuu93iuekd";
	private Logger logger = Logger.getLogger(AjaxReqController.class);

	@RequestMapping(value = "/getSecurityToken.htm", method = RequestMethod.POST)
	@ResponseBody
	public String getSecurityToken(@RequestParam(value="username", required = true) String username){
		System.out.println("getSecurityToken(" + username + ")");
		String token = SecurityTokenManager.getInstance().generateSecurityToken(username);
		return token;
	}
}
