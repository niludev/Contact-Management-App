package db;

import config.ApplicationContext;
import config.ApplicationProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() throws IOException {

        Connection connection = ApplicationContext.getInstance().getConnection();

        String sql = Files.readString(Path.of(ApplicationProperties.SCHEMA_FILE));

        try {

            Statement statement = connection.createStatement();

            String[] commands = sql.split(";");

            for (String command : commands) {
                String trimmed = command.trim();

                if (trimmed.isEmpty()) {
                    continue;
                }

                statement.executeUpdate(trimmed);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while initializing database schema", e);
        }
    }
}
