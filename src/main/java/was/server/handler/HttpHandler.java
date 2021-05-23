package was.server.handler;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simple.servlet.SimpleServlet;
import was.http.Constants;
import was.http.HttpRequest;
import was.http.HttpResponse;
import was.http.HttpStatus;
import was.server.module.ConfigModule;
import was.server.module.RouterModule;


/**
 * Created by bongkyunpark on 2020.05.23..
 */
public class HttpHandler implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(HttpHandler.class);
    private Socket connection;
    private static String ROOT_PATH;
    private InputStream input;
    private OutputStream output;
    HttpRequest request = null;
    HttpResponse response = null;

    public HttpHandler(Socket connection) {
        this.connection = connection;
    }

    
    @Override
    public void run() {
        
        // 상대경로 가져오기.
        File path = new File("");
        //SRC 소스 위치
        ROOT_PATH = path.getAbsolutePath();

        
        File file;

        try {
            input = this.connection.getInputStream();
            output = this.connection.getOutputStream();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        //1. request 처리
        request = new HttpRequest(input);
        request.parse();
        logger.info(request.toString());

        
        //spec1 HttpReuqst host주소와 맵핑되면 defeault server설정
        ConfigModule properties = ConfigModule.getInstance();
        // request.
        //http root 경로가 물리적인 경우는 그대로
//        if(properties.getDefaultServer().getHttproot().contains(":")) {
//            ROOT_PATH ="";
//        }
//        else {
//            //WAR배포 위치(classess위치)
//            ROOT_PATH = getClass().getResource("/").getPath().substring(1);
//        }
        
        //2. response 처리
        response = new HttpResponse(new BufferedOutputStream(output));

        
        
        byte[] data = new byte[0];
        try {
        	
            String filePath = request.getUrl();
            // spec 4. fileName의 확장자명이 .exe 일 경우 403 forbidden 처리
            // 확장자가.exe인파일을요청받았을때 (설정에 bat 도 추가해둠)
            if(properties.checkBlockedExtension(filePath)) {
                file = new File(ROOT_PATH + properties.getDefaultServer().getPage403());
                forbidden(response, file);
                logger.info(response.toString());
                return;
//                 throw new IOException("Not Access File!!");

            }
            
            // spec 4. 상위 경로 접근, 403 forbidden 처리
            // HTTP_ROOT 디렉터리의 상위 디렉토리에 접근할 때 (header에 상위폴더 접근자가 없음 - postman 으로도 확인함)
            // 예외처리 코드는 유지함
            if(properties.checkBlockRootPath(filePath)) {
                file = new File(ROOT_PATH + properties.getDefaultServer().getPage403());
                forbidden(response, file);
                logger.info(response.toString());
                return;
//              throw new IOException("Not Access Root!!");
            }

            if(filePath != null && filePath.indexOf("favicon.ico") > -1) {
                throw new FileNotFoundException("NOT favicon");
            }

            if (filePath.endsWith("/")) filePath += properties.getDefaultServer().getSource().getIndex();
            filePath = ROOT_PATH + properties.getDefaultServer().getHttproot() + filePath;
            file = new File(filePath);
            
            System.err.println("filePath :  "+ filePath);
            
            if (file.canRead()) {
                
                success(response, file);
                
            } else {
                
                // router 구현 (mapping.json)
                RouterModule routers = properties.getRouters();
                String packageName = routers.getUriPackage(request.getUrl());
                if(packageName != null) {
                    try {
                        
                        //servlet dispatcher
                        SimpleServlet sServlet = null;
                        String methodName = routers.getRouter(packageName).getRouter(request.getUrl());
                        Class<SimpleServlet> forName = ((Class<SimpleServlet>) Class.forName(packageName + "." + methodName));

                        try {
                            sServlet = forName.newInstance();
                        } catch (InstantiationException e) {
                            logger.error(e.getMessage(), e);
                        } catch (IllegalAccessException e) {
                            logger.error(e.getMessage(), e);
                        }

                        if(sServlet != null) {
                            response.setVersion(request.getHeader().getVersion());
                            response.setResponseCode("200 OK");
                            response.setContentType("text/html");
                            response.setLength(0);
                            response.sendHeader();
                            sServlet.service(request, response);
                            response.getWriter().flush();
                        }
                    } catch (ClassNotFoundException e) {
                        notFound(response, null);
//                        throw new Exception("Not Found Class");
                        
                    }
                } else {
                    file = new File(ROOT_PATH + properties.getDefaultServer().getPage404());
                    notFound(response, file);
                }
            }
            logger.info(response.toString());
        } catch (FileNotFoundException e) {
            logger.warn(e.getMessage(), e);
            try {
                file = new File(ROOT_PATH + properties.getDefaultServer().getPage404());
                notFound(response, file);
                logger.info(response.toString());
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        } catch (IOException e) {
            logger.warn(e.getMessage(), e);
            file = new File(ROOT_PATH + properties.getDefaultServer().getPage403());
            forbidden(response, file);
            logger.info(response.toString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            file = new File(ROOT_PATH + properties.getDefaultServer().getPage500());
            error(response, file);
            logger.info(response.toString());
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    

    public String makeHtmlByCode(String HttpCode) {

        StringBuilder sb = new StringBuilder();
        sb.append("<HTML>\r\n");
        sb.append("<HEAD><TITLE> " + HttpCode + " </TITLE>\r\n");
        sb.append("</HEAD>\r\n");
        sb.append("<BODY>\r\n");
        sb.append("<H1>HTTP Error " + HttpCode + " </H1>\r\n");
        sb.append("</BODY>\r\n");
        sb.append("</HTML>\r\n");
        sb.append("<HTML>\r\n");
        return sb.toString();
    }
    
    //spec2,3 403 - 출력HTML 맴핑,  http status 처리
    public void forbidden(HttpResponse response, File file) {
         byte[] data = new byte[0];
         try {
                 data = Files.readAllBytes(file.toPath());
             
        } catch (IOException e) {
             logger.error(e.getMessage(), e);
        }

         
         if( data.length == 0) {
             // can't find the file
            String body = this.makeHtmlByCode(HttpStatus.FORBIDDEN.toString());
            data = body.getBytes();
         }

         try {
             response.setVersion(request.getHeader().getVersion());
             response.setResponseCode(HttpStatus.FORBIDDEN.toString());
             response.setContentType(Constants.HTML_CONTENT_TYPE);
             response.setLength(data.length);
            response.sendHeader();
             response.getWriter().flush();
             response.send(data);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    //spec2,3 404 - 출력HTML 맴핑,  http status 처리
    public void notFound(HttpResponse response, File file) {
        byte[] data = new byte[0];
        try {
               data = Files.readAllBytes(file.toPath());
            
        } catch (IOException e) {
             logger.error(e.getMessage(), e);
        }
        
         if( data.length == 0) {
            // can't find the file
           String body = this.makeHtmlByCode(HttpStatus.NOT_FOUND.toString());
           data = body.getBytes();
         }

        try {
            response.setVersion(request.getHeader().getVersion());
            response.setResponseCode(HttpStatus.NOT_FOUND.toString());
            response.setContentType(Constants.HTML_CONTENT_TYPE);
            response.setLength(data.length);
            response.sendHeader();
             response.getWriter().flush();
             response.send(data);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
   }
  //spec2,3 500 - 출력HTML 맴핑,  http status 처리
    public void error(HttpResponse response, File file) {
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(file.toPath());
            
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        
         if( data.length == 0) {
            // can't find the file
            String body = this.makeHtmlByCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            data = body.getBytes();
        }
        
        try {
            response.setVersion(request.getHeader().getVersion());
            response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
            response.setContentType(Constants.HTML_CONTENT_TYPE);
            response.setLength(data.length);
            response.sendHeader();
            response.getWriter().flush();
            response.send(data);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
    
    public void success(HttpResponse response, File file) {
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        
         if( data.length == 0) {
            // can't find the file
            String body = this.makeHtmlByCode(HttpStatus.OK.toString());
            data = body.getBytes();
        }
        
        try {
            response.setVersion(request.getHeader().getVersion());
            response.setResponseCode(HttpStatus.OK.toString());
            
            //todo, file유형별로 contenttype 다르게 해줘야함
            response.setContentType(Constants.HTML_CONTENT_TYPE);
            response.setLength(data.length);
            response.sendHeader();
            response.getWriter().flush();
            response.send(data);
            
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
    
}
