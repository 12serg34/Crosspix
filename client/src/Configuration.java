import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static final String HOST = "server.host";
    private static final String PORT = "server.port";
    private static final String CLIENT_PROPERTIES = "client.properties";

    private String host;
    private int port;

    public void readProperties() {
        Properties properties = loadPropertiesFromResourceFile();
        host = properties.getProperty(HOST);
        port = Integer.parseInt(properties.getProperty(PORT));
    }

    private Properties loadPropertiesFromResourceFile() {
        Properties properties = new Properties();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream propertiesAsStream = classLoader.getResourceAsStream(CLIENT_PROPERTIES);
        try {
            properties.load(propertiesAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
