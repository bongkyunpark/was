package was.server.module;

import java.io.IOException;
import java.util.*;

import was.server.vo.Server;
import was.server.vo.Source;
import was.util.JsonUtil;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by bongkyunpark on 2020.05.23
 */
public class ServerModule {
    private static Logger logger = LoggerFactory.getLogger(ServerModule.class);
    private Map<String, Server> srvs = new HashMap<String, Server>();
    private Server defaultServer;

	//spec 2 was config json
    public void buildServer(JSONObject mapper) throws IOException {
        JSONArray jsonArray = (JSONArray) mapper.get("servers");
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObj = (JSONObject)jsonArray.get(i);
            
            // server_name, domain, http_root
            Server srv = new Server(JsonUtil.getString(jsonObj, "server_name"));
            srv.setDomain(JsonUtil.getString(jsonObj, "server_name"));
            srv.setHttproot(JsonUtil.getString(jsonObj, "http_root"));
            
            JSONObject jsonSubObj = (JSONObject) jsonObj.get("source");
            Source source = new Source();
            source.setIndex(JsonUtil.getString(jsonSubObj, "index"));
            source.set403(JsonUtil.getString(jsonSubObj, "403"));
            source.set404(JsonUtil.getString(jsonSubObj, "404"));
            source.set500(JsonUtil.getString(jsonSubObj, "500"));
            srv.setSource(source);
            
            srvs.put(srv.getName(), srv);
            
        }
    }
    

    public Server getDefaultServer() {
        return this.defaultServer;
    }

    /**
     * config 설정을 따라 적용되어야 되기에 String를 파라메터로 받음
     * @param srvName
     */
    public void setDefaultServer(String srvName) {
        Server srv = srvs.get(srvName);
        this.defaultServer = srv;
    }

    public Server getServer(String srvName) {
        Server srv = srvs.get(srvName);
        return srv;
    }

    //spec1 host별 server설정
    public void setDefaultServerDomain(String domain) {
        if(!this.getDefaultServer().getDomain().equals(domain)) {
            for( Map.Entry<String, Server> srv : srvs.entrySet() ){
                if(srv.getValue().getDomain().equals(domain))
                    this.defaultServer = srv.getValue();
            }
        }
    }

    public Integer getServerCount() {
        return srvs.size();
    }
}