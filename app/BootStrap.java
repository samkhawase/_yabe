
import play.*;
import play.jobs.*;
import play.test.*; 
import models.*;
import org.apache.log4j.*;
import org.apache.log4j.Logger;

@OnApplicationStart
public class BootStrap extends Job {
 
	private static Logger logger = LogManager.getLogger(BootStrap.class);
    public void doJob() {
        // Check if the database is empty
        if(User.count() == 0) {
            Fixtures.loadModels("initial-data.yml");
        }
    }
 
}
