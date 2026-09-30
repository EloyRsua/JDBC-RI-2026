package uo.ri.util.jdbc;

public class Queries {
    private static final PropertyFile config = new PropertyFile(
            "queries.properties");

    public static String getSQLSentence(String key) {
        return config.getRequired(key);
    }
}