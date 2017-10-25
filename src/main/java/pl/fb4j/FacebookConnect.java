package pl.fb4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.auth.OAuthAuthorization;
import facebook4j.auth.OAuthSupport;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;
import facebook4j.internal.logging.Logger;


@Controller
@RequestMapping ("")
public class FacebookConnect {

	Logger logger;
	
	String protect = "protection_of"+ Math.random()*1000 +"my_connection";

	@GetMapping ("/")
	public String connectToFacebook (Model model) throws FacebookException {
		model.addAttribute("facebook", "Guest");
		model.addAttribute("login", "notLogged");
		return "signin";
	}
	
	@GetMapping ("/signin1")
	public String connectedToFacebook (Model model) throws FacebookException {
		
		
		// gets current uri - to be used when app is deployed
//		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
//		builder.scheme("https");
//		builder.replaceQueryParam("someBoolean", false);
//		String myUri = builder.build().toUri().toString();
//		
		
		Facebook facebook = GetFacebookInstance();
		if (facebook == null) {
			model.addAttribute("facebook", "Guest+");
			model.addAttribute("login", "notLogged");
			return "signin";
		}
		System.err.println(facebook.toString());		
	
		SessionedController.session().setAttribute("ExternalURL", 
				facebook.getOAuthAuthorizationURL(facebook.getOAuthCallbackURL().toString())); 
		return "redirect:/to-be-redirected" ;
	}
	
	@RequestMapping("/to-be-redirected")
	public RedirectView localRedirect() {
	    RedirectView redirectView = new RedirectView();
	    redirectView.setUrl((String) SessionedController.session().getAttribute("ExternalURL"));
	    return redirectView;
	}

	@GetMapping ("/signin")
	public String callBack (Model model) throws FacebookException {
	
	 Facebook facebook = (Facebook) SessionedController.session().getAttribute("facebook");
     String oauthCode = (String) SessionedController.session().getAttribute("code");
     facebook.getOAuthAccessToken(oauthCode);
 	model.addAttribute("facebook", "loged in to facebook");
	model.addAttribute("login", "logged");
	return "signin";
	}
	
	@GetMapping ("/main")
	public String enterMainPage (Model model) throws FacebookException {
	
	 Facebook facebook = (Facebook) SessionedController.session().getAttribute("facebook");
  	model.addAttribute("facebook", facebook.getName());
	model.addAttribute("login", "logged");
	return "main";
	}
	
	
	public Facebook GetFacebookInstance() throws FacebookException {
		
		// gets current uri - to be used when app is deployed. When tested this uri shoul base on ngrok address
//		String callbackURL =  myUri + "/signup";

		AccessToken  accessToken = null;
		Configuration configuration = createConfiguration("http://82b394dd.ngrok.io/signin");
	    FacebookFactory facebookFactory = new FacebookFactory(configuration );
	    Facebook facebookClient = facebookFactory.getInstance();
	    OAuthSupport oAuthSupport = new OAuthAuthorization(configuration ); 
	    accessToken = oAuthSupport.getOAuthAppAccessToken();
	    
	    System.err.println("acces token" + accessToken.toString());
	    facebookClient.setOAuthAccessToken( accessToken );
	    facebookClient.setOAuthCallbackURL("http://82b394dd.ngrok.io/signin");
	    
	    // thito be used to extend session duration
//	    String reAuthUrl = facebookClient.getOAuthReAuthenticationURL("http://f20b5496.ngrok.io/signin", protect);
	
	    return facebookClient;
	    
		}

	
	
	public Configuration createConfiguration(String callbackURL) {
        ConfigurationBuilder configurationBuilder  = new ConfigurationBuilder();	
	
			configurationBuilder.setDebugEnabled(true);
			configurationBuilder.setOAuthAppId("306746906469019");
			configurationBuilder.setOAuthAppSecret("8eea6d44a60f063b0e9b54556326ee81");
			configurationBuilder
					.setOAuthPermissions("email, publish_stream, id, name, first_name, last_name, read_stream , generic, user_posts, user_likes");
			configurationBuilder.setUseSSL(true);
			configurationBuilder.setJSONStoreEnabled(true);
//			configurationBuilder.setOAuthAccessToken("EAACEdEose0cBANNfh2JIkP0XjD0ZBDKqBZCW0RErfCyLMjOWdoz1ZCG77O8UssB4mbMdrA31gPqZCPwFv4v6XqmcMCl7cZCqbxWFako3ZBCDRrwgerOP2jJbA9GpnFSYG1ZClUqWLZA7LY7cBIdbVpWfdKuZChAhh6OiR9Qqw8U1OPdubkZAGKH4m1Rpe3A30FZBp5l0dBjE6YivwZDZD");
			configurationBuilder.setRestBaseURL("https://graph.facebook.com/v2.10/");
			configurationBuilder.setClientURL(callbackURL);

			Configuration configuration = configurationBuilder.build();
	        return configuration;
	}			
	
}
