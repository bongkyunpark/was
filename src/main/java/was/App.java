package was;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import was.webserver.HttpServer;



/**
 * was 구동 
 * 단순히 root폴더에 html 로딩 기능만 수행함.
 * @author bongkyunpark@gmail.com
 *
 */
public class App 
{
    private static Logger logger = LoggerFactory.getLogger(App.class);
    
    /*
     * 
     * param1 : http root폴더
     * param2 : port
     */
    public static void main( String[] args )
    {
    	String root_path = args[0];

    	// get the Document root
        File docroot;
        try {
//            docroot = new File(args[0]);
            docroot = new File(root_path);
        } catch (ArrayIndexOutOfBoundsException ex) {
        	logger.error("Usage: java JHTTP docroot port");
            return;
        }
        // set the port to listen on
        int port;
        try {
            port = Integer.parseInt(args[1]);
            if (port < 0 || port > 65535) port = 80;
        } catch (RuntimeException ex) {
            port = 80;
        }
        try {
            HttpServer webserver = new HttpServer(docroot, port);
            
            logger.info("HTTP Server Starting");
            webserver.start();
        } catch (IOException ex) {
            logger.error("Server could not start", ex);
        }
    }
}
