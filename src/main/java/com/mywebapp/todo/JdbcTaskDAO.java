package com.mywebapp.todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTaskDAO implements TaskDAO {
    private final String url;

    public JdbcTaskDAO(String url) throws SQLException {
        this.url = url;
        // load the driver class explicitly; Tomcat's classloading sometimes
        // prevents automatic registration via ServiceLoader
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE TABLE IF NOT EXISTS tasks (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, completed INTEGER NOT NULL DEFAULT 0)");
            }
        }
    }

    @Override
    public List<Task> findAll() throws SQLException {
        List<Task> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("SELECT id, title, completed FROM tasks ORDER BY id")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Task t = new Task(rs.getInt("id"), rs.getString("title"), rs.getInt("completed") != 0);
                    list.add(t);
                }
            }
        }
        return list;
    }

    @Override
    public Optional<Task> findById(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("SELECT id, title, completed FROM tasks WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Task t = new Task(rs.getInt("id"), rs.getString("title"), rs.getInt("completed") != 0);
                    return Optional.of(t);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Task create(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, completed) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, task.getTitle());
            ps.setInt(2, task.isCompleted() ? 1 : 0);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Creating task failed, no rows affected.");
            }
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    task.setId(keys.getInt(1));
                }
            }
        }
        return task;
    }

    @Override
    public boolean update(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title=?, completed=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setInt(2, task.isCompleted() ? 1 : 0);
            ps.setInt(3, task.getId());
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}
