package was.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import was.server.handler.HttpHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by bongkyunpark on 2020.05.23
 */
public class HttpServer {
    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private static final int NUM_THREADS = 50;
    private int port = 80;

    public HttpServer(int port) throws IOException {
        this.port = port;
    }

    public void start() throws Exception {
        // TODO:: config 설정의 서버 갯수에 따라 Server을 여러개 띄우는 로직이 구현되어야..
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        try (ServerSocket server = new ServerSocket(port)) {
            
        	while (true) {
                try {
                    Socket request = server.accept();
                    Runnable r = new HttpHandler(request);
                    pool.submit(r);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
