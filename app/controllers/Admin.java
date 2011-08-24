package controllers;

import play.mvc.*;
import java.util.*;
import models.*;
import org.apache.log4j.*;

@With(Secure.class)
public class Admin extends Controller {

	private static Logger logger = LogManager.getLogger(Admin.class);
	
    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.fullName);
//            logger.info("User attached: "+user.fullName);
        }
    }
 
    public static void index() {
        render();
    }
    
}
