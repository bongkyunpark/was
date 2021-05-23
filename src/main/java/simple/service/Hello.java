package simple.service;

import java.io.IOException;
import java.io.Writer;

import simple.servlet.SimpleServlet;
import was.http.HttpRequest;
import was.http.HttpResponse;

/**
 * Created by bongkyunpark on 2020.05.23
 */
//spec 6 Simplet servet 구현체 - hello
public class Hello implements SimpleServlet {
    @Override
    public void service(HttpRequest req, HttpResponse res) throws IOException {
        System.out.print(res);
        Writer writer = res.getWriter();
        writer.write("service.Hello, ");
        writer.write(req.getParameter("name"));
        writer.flush();
    }
    
    
    public void error(HttpRequest req, HttpResponse res) throws Exception {
        System.out.print(res);
        throw new Exception("500 TEST");
    }
}
