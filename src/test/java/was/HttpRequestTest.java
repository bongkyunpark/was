package was;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import was.http.HttpRequest;



public class HttpRequestTest {

    private final String httpHeader = "GET /index.html HTTP/1.1\r\n" +
            "Host: localhost:8080\r\n" +
            "Connection: keep-alive\r\n" +
            "Accept: */*\r\n" +
            "\r\n";	
    private HttpRequest httpRequest;
    
  @Before
  public void setUp() throws Exception {
  	httpRequest = new HttpRequest(new ByteArrayInputStream(httpHeader.getBytes()));
  	httpRequest.parse();
  }
    
    @Test
    public void _01_test_getVersion() throws IOException, ParseException {
    	assertThat(httpRequest.getHeader().getVersion(), is("HTTP/1.1"));
    }
    
    @Test
    public void _02_test_GetMethod() throws IOException {
    	assertThat(httpRequest.getHeader().getMethod(), is("GET"));
    }

    @Test
    public void _03_test_GetPath() throws IOException {
    	assertThat(httpRequest.getUrl(), is("/index.html"));
    }
}