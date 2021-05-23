package simple;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import simple.servlet.SimpleServlet;
import was.http.HttpRequest;
import was.http.HttpResponse;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bongkyunpark on 2020.05.23..
 */

//spec 7 Simplet servet 구현체 - Time
public class TimeTest implements SimpleServlet {
    private static Logger logger = LoggerFactory.getLogger(Hello.class);
    @Override
    public void service(HttpRequest req, HttpResponse res) throws IOException {
        Writer writer = res.getWriter();
        long time = System.currentTimeMillis();
     // spec 7. 현재 시각을 출력 하는 구현체
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        writer.write(dayTime.format(new Date(time)));
    }
}