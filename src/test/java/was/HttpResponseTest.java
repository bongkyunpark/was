package was;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;


import java.io.File;
import java.io.FileOutputStream;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import was.http.HttpRequest;
import was.http.HttpResponse;
import was.server.handler.HttpHandler;



public class HttpResponseTest {

    private final String httpHeader = "GET /index.html HTTP/1.1\r\n" +
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
       httpRequest = new HttpRequest(new ByteArrayInputStream(httpHeader.getBytes()));
       httpRequest.parse();
    }


    @Test
    public void _01_test_forbidden() throws IOException, ParseException {
    	httpResponse = new HttpResponse(createOutputStream("Http_forbidden.txt"));
    	httpHandler.forbidden(httpResponse, null);
    	
    }
    
    @Test
    public void _02_test_forbidden_html() throws IOException, ParseException {
    	httpResponse = new HttpResponse(createOutputStream("Http_forbidden.txt"));
    	httpHandler.forbidden(httpResponse, new File(testDirectory + "403.html"));
    	
    }

    @Test
    public void _03_test_notfound() throws IOException, ParseException {
    	httpResponse = new HttpResponse(createOutputStream("Http_notFound.txt"));
    	httpHandler.notFound(httpResponse, null);
    	
    }
    @Test
    public void _04_test_notfound_html() throws IOException, ParseException {
    	httpResponse = new HttpResponse(createOutputStream("Http_notFound.tx"));
    	httpHandler.forbidden(httpResponse, new File(testDirectory + "404.html"));
    	
    }
    

    @Test
    public void _05_test_error() throws IOException, ParseException {
    	httpResponse = new HttpResponse(createOutputStream("Http_error.txt"));
    	httpHandler.error(httpResponse, null);
    	
    }
    @Test
    public void _06_test_error_html() throws IOException, ParseException {
    	httpResponse = new HttpResponse(createOutputStream("Http_error.txt"));
    	httpHandler.error(httpResponse, new File(testDirectory + "404.html"));
    	
    }
    
    @Test
    public void _07_test_index() throws IOException, ParseException {
    	httpResponse = new HttpResponse(createOutputStream("Http_success.txt"));
    	httpHandler.error(httpResponse, null);
    	
    }
    
    @Test
    public void _08_test_index_html() throws IOException, ParseException {
    	httpResponse = new HttpResponse(createOutputStream("Http_success.txt"));
    	httpHandler.error(httpResponse, new File(testDirectory + "index.html"));
    	
    }
    

    
//    
    private OutputStream createOutputStream(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(testDirectory + filename));
    }
  
}