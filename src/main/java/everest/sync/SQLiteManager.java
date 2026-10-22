package everest.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteManager {
    private static final Logger log = LoggerFactory.getLogger(SQLiteManager.class);
    private static Connection connection;
    private static final String DB_PATH = System.getProperty("user.home") + "/.everest/history.sqlite";

    public static synchronized void initialize() {
        try {
            var dir = new java.io.File(System.getProperty("user.home"), ".everest");
            if (!dir.exists()) dir.mkdirs();

            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            createTables();
            log.info("SQLite database initialized at {}", DB_PATH);
        } catch (SQLException e) {
            log.error("Failed to initialize SQLite database", e);
        }
    }

    private static void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS requests (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    method TEXT NOT NULL,
                    url TEXT NOT NULL,
                    headers TEXT,
                    body TEXT,
                    content_type TEXT,
                    status_code INTEGER,
                    response_body TEXT,
                    response_time_ms INTEGER,
                    response_size INTEGER,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
        }
    }

    public static void saveRequest(String method, String url, String headers, String body,
                                    String contentType, int statusCode, String responseBody,
                                    long responseTimeMs, int responseSize) {
        String sql = "INSERT INTO requests (method, url, headers, body, content_type, status_code, response_body, response_time_ms, response_size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, method);
            pstmt.setString(2, url);
            pstmt.setString(3, headers);
            pstmt.setString(4, body);
            pstmt.setString(5, contentType);
            pstmt.setInt(6, statusCode);
            pstmt.setString(7, responseBody);
            pstmt.setLong(8, responseTimeMs);
            pstmt.setInt(9, responseSize);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Failed to save request to history", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static synchronized void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                log.info("SQLite connection closed");
            }
        } catch (SQLException e) {
            log.error("Failed to close SQLite connection", e);
        }
    }
}
