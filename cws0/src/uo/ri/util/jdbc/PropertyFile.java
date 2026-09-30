package uo.ri.util.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class PropertyFile {
    private final Properties props;
    private final String fileName;

    public PropertyFile(String fileName) {
        this.fileName = fileName;
        this.props = new Properties();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalArgumentException(
                        "Properties file not found: " + fileName);
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error loading properties file: " + fileName,
                    e);
        }
    }

    /**
     * Returns the value of a key that must be present. If the value is an empty
     * string (""), it is still returned.
     */
    public String getRequired(String key) {
        if (!props.containsKey(key)) {
            throw new IllegalArgumentException(
                    "Missing property key '" + key + "' in file: " + fileName);
        }
        return props.getProperty(key); // puede ser "", y eso está bien
    }

    /**
     * Returns the value if present, or a default value if not.
     */
    public String getOrDefault(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    /**
     * Returns an Optional<String> containing the value (empty if explicitly
     * defined as such), or an empty Optional if the key does not exist.
     */
    public Optional<String> getOptional(String key) {
        return props.containsKey(key)
                ? Optional.ofNullable(props.getProperty(key))
                : Optional.empty();
    }

    public boolean containsKey(String key) {
        return props.containsKey(key);
    }
}