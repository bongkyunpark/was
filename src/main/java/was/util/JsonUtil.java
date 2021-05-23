package was.util;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bongkyunpark on 2020.05.23..
 */
public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String getString(JSONObject obj, String key) {
       String rts  ="";
       
       if(obj != null && obj.containsKey(key)) {
    	   rts = obj.get(key).toString();
       }
       return rts; 
    }
    
    public static Integer getInt(JSONObject obj, String key)  {
    	Integer rti  = null;
        
        return rti; 
     }
}
