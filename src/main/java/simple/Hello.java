package simple;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simple.servlet.SimpleServlet;
import was.http.HttpRequest;
import was.http.HttpResponse;

import java.io.IOException;
import java.io.Writer;

/**
 * Created by bongkyunpark on 2020.05.23..
 */
//spec 6 Simplet servet 구현체 - hello
public class Hello implements SimpleServlet {
    private static Logger logger = LoggerFactory.getLogger(Hello.class);
    @Override
    public void service(HttpRequest req, HttpResponse res) throws IOException {
        Writer writer = res.getWriter();
        writer.write("Hello, ");
        writer.write(req.getParameter("name"));
    }
    public void error(HttpRequest req, HttpResponse res) throws Exception {
        System.out.print(res);
        throw new Exception("500 TEST");
    }
}
