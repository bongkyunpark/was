package simple.servlet;

import was.http.HttpRequest;
import was.http.HttpResponse;

/**
 * Created by bongkyunpark on 2020.05.23..
 */
//spec 6 simple Servlet 구현체
public interface SimpleServlet {
    void service(HttpRequest req, HttpResponse res) throws Exception;
}
