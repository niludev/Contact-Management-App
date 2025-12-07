package config;

public class ApplicationProperties {

    public static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/appdb";
    public static final String DATABASE_USER = "appuser";
    public static final String DATABASE_PASSWORD = "apppass";
    public static final String SCHEMA_FILE = "schema.sql";

    private ApplicationProperties() {}
}
