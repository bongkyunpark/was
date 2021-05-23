package was;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import was.http.HttpResponse;
import was.server.module.ConfigModule;
import was.server.module.ServerModule;



public class HttpServerTest {

	
    private String defaultName;
    private ServerModule servers;
    private int PORT;
    private ConfigModule properties;
    
  @Before
  public void setUp() throws Exception {
      // server01, server02 설정 파일 분리.
       properties = ConfigModule.getInstance();
  }
    
    @Test
    public void _01_test_configLoading() throws IOException, ParseException {
        
    	// singleton 패턴으로 static configuration 구현.
//        try {
////            String fileContent = FileUtil.getFileContent("wasconfig.json");
//            String jsonDirectory = ".\\src\\test\\resources\\wasconfig.json";
//            
//            JSONParser parser = new JSONParser();
//            Object obj = parser.parse(new FileReader(jsonDirectory));
////            Object obj = parser.parse(fileContent);
//            JSONObject mapper = (JSONObject) obj;
//            defaultName = mapper.get("default_name").toString();
//            
//            if(mapper.get("port") != null) {
//            PORT = Integer.parseInt(mapper.get("port").toString());
//            }
//            
//            servers = new ServerModule();
//            servers.buildServer(mapper);
//            servers.setDefaultServer(defaultName);
//            
//            
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //port확인
        int port;
        port = properties.getPort();
        assertThat(port, is(8080));

        //http_root 확인
        String root;
        
//        root = servers.getDefaultServer().getHttproot();
        root = properties.getDefaultServer().getHttproot();
        assertThat(root, is("/src/main/resources/webapp1/WEB-INF"));
    }
    
    @Test
    public void _02_test_blocked_extension() throws IOException, ParseException {
//    	String fileName = "/index.exe";
    	String fileName = "/index.html";
    	assertThat(properties.checkBlockedExtension(fileName), is(Boolean.FALSE));
    }
    @Test
    public void _03_test_block_rootPath() throws IOException, ParseException {
//    	String fileName = "/../../etc/download";
    	String fileName = "/etc/download";
    	assertThat(properties.checkBlockRootPath(fileName), is(Boolean.FALSE));
    }
}