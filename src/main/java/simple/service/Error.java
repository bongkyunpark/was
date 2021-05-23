package simple.service;

import simple.servlet.SimpleServlet;
import was.http.HttpRequest;
import was.http.HttpResponse;

/**
 * Created by bongkyunpark on 2020.05.23
 */
//spec 6 Simplet servet 구현체 - hello
public class Error implements SimpleServlet {
    @Override
    public void service(HttpRequest req, HttpResponse res) throws Exception {
        System.out.print(res);
        throw new Exception("500 TEST");
    }
    
}
