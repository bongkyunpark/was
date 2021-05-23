package was.server.module;

import java.io.IOException;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.LoggerFactory;


import was.server.vo.Server;
import was.util.FileUtil;

/**
 * Created by bongkyunpark on 2020.05.23
 */
public class ConfigModule {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigModule.class);
    private static String defaultName;
    private static ServerModule servers;
    private static int PORT;
    private static String[] blockedExtension;
    private static ConfigModule instance = null;
    private static RouterModule routers;
    private  String configFileName = "wasconfig.json";
    private  String mappingFileName = "mapping.json";
    
    private ConfigModule() throws Exception {

    	//spec 2 was config json
        try {
            String fileContent = FileUtil.getFileContent(configFileName);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(fileContent);
            JSONObject mapper = (JSONObject) obj;
            defaultName = mapper.get("default_name").toString();
            blockedExtension =  mapper.get("blocked_extension").toString().split(",");
            
            if(mapper.get("port") != null) {
            PORT = Integer.parseInt(mapper.get("port").toString());
            }
            
            servers = new ServerModule();
            servers.buildServer(mapper);
            servers.setDefaultServer(defaultName);
            
            
        } catch (IOException e) {
            logger.warn("error: WAS config json loading error", e);
        }


        //spec 6 simple servlet
        try {
            String fileContent = FileUtil.getFileContent(mappingFileName);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(fileContent);
            JSONObject mapper = (JSONObject) obj;
            routers = new RouterModule();
            routers.buildRouter(mapper);
        } catch (IOException e) {
            logger.warn("error: servlet mapping json loading ", e);
        }
        
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ConfigModule getInstance() {
        if(instance == null) {
            try {
                instance = new ConfigModule();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return ConfigModule.instance;
    }
    
    

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort() {
        return PORT;
    }

    /**
     * Gets servers.
     *
     * @return the servers
     */
    public ServerModule getServers() {
        return servers;
    }

    /**
     * Gets routers.
     *
     * @return the routers
     */
    public RouterModule getRouters() {
        return routers;
    }

    /**
     * Gets default server.
     *
     * @return the default server
     */
    public Server getDefaultServer() {
        return this.getServers().getDefaultServer();
    }

    /**
     * Check blocked extension boolean.
     *
     * @param fileName the file name
     * @return the boolean
     * @throws ArrayIndexOutOfBoundsException the array index out of bounds exception
     */
    public Boolean checkBlockedExtension(String fileName) throws ArrayIndexOutOfBoundsException {
        if(fileName == null) {
            return false;
        }
        if(fileName.indexOf(".") > -1) {
            String[] fileToken = fileName.split("\\.");
            return Arrays.asList(blockedExtension).contains(fileToken[1]);
        }
        if (fileName.indexOf("../") > -1) {
            return true;
        }
        return false;
    }

    /**
     * Check block root path boolean.
     *
     * @param fileName the file name
     * @return the boolean
     */
    public Boolean checkBlockRootPath(String fileName) {
        if(fileName == null) {
            return false;
        }
        if (fileName.indexOf("../") > -1) {
            return true;
        }
        return false;
    }

	public String getConfigFileName() {
		return configFileName;
	}

	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	public String getMappingFileName() {
		return mappingFileName;
	}

	public void setMappingFileName(String mappingFileName) {
		this.mappingFileName = mappingFileName;
	}
    
    
}
