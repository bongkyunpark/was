package simple;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.junit.Before;
import org.junit.Test;

import was.http.HttpRequest;
import was.http.HttpResponse;
import was.server.handler.HttpHandler;



public class SimpleServletTest {

    private String httpHeader = "GET /Hello HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Connection: keep-alive\r\n" +
            "Accept: */*\r\n" +
            "\r\n";	
    private String serviceHellohttpHeader = "GET /service.Hello HTTP/1.1\r\n" +
    		"Host: localhost:8080\r\n" +
    		"Connection: keep-alive\r\n" +
    		"Accept: */*\r\n" +
    		"\r\n";	
    private String errorhttpHeader = "GET /error.Hello HTTP/1.1\r\n" +
    		"Host: localhost:8080\r\n" +
    		"Connection: keep-alive\r\n" +
    		"Accept: */*\r\n" +
    		"\r\n";	    
    private String timehttpHeader = "GET /time HTTP/1.1\r\n" +
    		"Host: localhost:8080\r\n" +
    		"Connection: keep-alive\r\n" +
    		"Accept: */*\r\n" +
    		"\r\n";	
    private HttpRequest httpRequest;
    
    private String testDirectory = "./src/test/resources/webapp/test/";
    private HttpResponse httpResponse;
    
    private HttpHandler httpHandler;
    
  @Before
  public void setUp() throws Exception {

  	
    httpHandler = new HttpHandler(null);
  }
    
    @Test
    public void _01_test_hello() throws Exception {
      	httpRequest = new HttpRequest(new ByteArrayInputStream(httpHeader.getBytes()));
      	httpRequest.parse();
      	
    	httpResponse = new HttpResponse(createOutputStream("Http_success.txt"));
       	httpHandler.dispatch(httpRequest, httpResponse);
    }
    
    @Test
    public void _02_test_service_hello() throws Exception {
    	
      	httpRequest = new HttpRequest(new ByteArrayInputStream(serviceHellohttpHeader.getBytes()));
      	httpRequest.parse();
      	
    	httpResponse = new HttpResponse(createOutputStream("Http_success.txt"));
    	httpHandler.dispatch(httpRequest, httpResponse);
    }
    
    @Test
    public void _03_test_time() throws Exception {
      	httpRequest = new HttpRequest(new ByteArrayInputStream(timehttpHeader.getBytes()));
      	httpRequest.parse();
      	
    	httpResponse = new HttpResponse(createOutputStream("Http_success.txt"));
    	httpHandler.dispatch(httpRequest, httpResponse);
    }
    
    
    
    @Test
    public void _04_test_error() throws Exception {
    	httpRequest = new HttpRequest(new ByteArrayInputStream(errorhttpHeader.getBytes()));
    	httpRequest.parse();
    	
    	httpResponse = new HttpResponse(createOutputStream("Http_success.txt"));
    	httpHandler.dispatch(httpRequest, httpResponse);
    }
    
    
//  
  private OutputStream createOutputStream(String filename) throws FileNotFoundException {
      return new FileOutputStream(new File(testDirectory + filename));
  }

    
}