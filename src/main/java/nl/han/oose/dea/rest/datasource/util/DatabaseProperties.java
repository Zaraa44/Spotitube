package nl.han.oose.dea.rest.datasource.util;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.rest.services.exceptions.DatabaseException;

import java.io.IOException;
import java.util.Properties;

@ApplicationScoped
public class DatabaseProperties {
    private Properties properties;

    public DatabaseProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            throw new DatabaseException();
        }
    }

    public String connectionString() {
        return properties.getProperty("connectionString");
    }
}
