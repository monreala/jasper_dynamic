package org.example.data;

import org.example.model.Holiday;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class H2Database implements AutoCloseable {

    private static final String JDBC_URL = "jdbc:h2:mem:holidays;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private final Connection connection;

    public H2Database() throws SQLException {
        this.connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        createSchema();
        populate(HolidaysData.holidays2021());
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        Statement statement = connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        return statement.executeQuery(sql);
    }

    private void createSchema() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS specialdate");
            st.executeUpdate("""
                    CREATE TABLE specialdate (
                        id      IDENTITY PRIMARY KEY,
                        country VARCHAR(8)   NOT NULL,
                        name    VARCHAR(128) NOT NULL,
                        data    DATE         NOT NULL
                    )
                    """);
        }
    }

    private void populate(List<Holiday> holidays) throws SQLException {
        String sql = "INSERT INTO specialdate (country, name, data) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Holiday h : holidays) {
                ps.setString(1, h.getCountry());
                ps.setString(2, h.getName());
                ps.setDate(3, new java.sql.Date(h.getDate().getTime()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
