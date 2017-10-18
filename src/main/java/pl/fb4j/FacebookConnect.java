package pl.fb4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
@RequestMapping ("connect")
public class FacebookConnect {

	Logger logger;
	
	String protect = "protection_of"+ Math.random()*1000 +"my_connection";

	@GetMapping ("/")
	public String connectToFacebook (Model model) throws FacebookException {
		
		return "signin";
	}
	
	@GetMapping ("/connected")
	public String connectedToFacebook (@RequestParam String myURL, Model model) throws FacebookException {
		
		System.err.println(myURL);
		Facebook facebook = GetFacebookInstance(myURL);
		model.addAttribute("facebook", facebook);
		model.addAttribute("login", "logged");
		return "signin";
	}
	
	
	
	public Facebook GetFacebookInstance(String myURL) throws FacebookException {
		
		String callbackURL =  myURL + "/signup";

		AccessToken  accessToken = null;
		Configuration configuration = createConfiguration(callbackURL);
	    FacebookFactory facebookFactory = new FacebookFactory(configuration );
	    Facebook facebookClient = facebookFactory.getInstance();
	//    OAuthSupport oAuthSupport = new OAuthAuthorization(configuration ); 
	//    accessToken = oAuthSupport.getOAuthAppAccessToken();
	//    facebookClient.setOAuthAccessToken( accessToken );
	//    
	    String reAuthUrl = facebookClient.getOAuthReAuthenticationURL(callbackURL, protect);
	
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
			configurationBuilder.setOAuthAccessToken("EAACEdEose0cBANNfh2JIkP0XjD0ZBDKqBZCW0RErfCyLMjOWdoz1ZCG77O8UssB4mbMdrA31gPqZCPwFv4v6XqmcMCl7cZCqbxWFako3ZBCDRrwgerOP2jJbA9GpnFSYG1ZClUqWLZA7LY7cBIdbVpWfdKuZChAhh6OiR9Qqw8U1OPdubkZAGKH4m1Rpe3A30FZBp5l0dBjE6YivwZDZD");
			configurationBuilder.setRestBaseURL("https://graph.facebook.com/v2.10/");
			configurationBuilder.setClientURL(callbackURL);

			Configuration configuration = configurationBuilder.build();
	        return configuration;
	}			
	
}
