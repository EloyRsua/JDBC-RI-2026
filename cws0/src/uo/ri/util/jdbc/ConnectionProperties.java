package uo.ri.util.jdbc;

public class ConnectionProperties {
    private static final PropertyFile config = new PropertyFile(
            "connection.properties");

    public static String getProperty(String key) {
        return config.getRequired(key);
    }
}
