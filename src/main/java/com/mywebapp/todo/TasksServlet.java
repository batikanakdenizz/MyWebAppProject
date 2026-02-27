package com.mywebapp.todo;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/tasks/*")
public class TasksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TaskDAO dao;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            String dbUrl = "jdbc:sqlite:todo.db";
            this.dao = new JdbcTaskDAO(dbUrl);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json");
        try {
            if (path == null || path.equals("/")) {
                List<Task> tasks = dao.findAll();
                mapper.writeValue(resp.getOutputStream(), tasks);
            } else {
                String idStr = path.replaceFirst("/", "");
                int id = Integer.parseInt(idStr);
                dao.findById(id).ifPresentOrElse(t -> {
                    try {
                        mapper.writeValue(resp.getOutputStream(), t);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }, () -> {
                    try {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    } catch (Exception ignored) {
                    }
                });
            }
        } catch (NumberFormatException nf) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Task incoming;
            String contentType = req.getContentType();
            if (contentType != null && contentType.startsWith("application/x-www-form-urlencoded")) {
                // handle simple form-posted data
                String title = req.getParameter("title");
                boolean completed = Boolean.parseBoolean(req.getParameter("completed"));
                incoming = new Task(title, completed);
            } else {
                // assume JSON
                incoming = mapper.readValue(req.getInputStream(), Task.class);
            }
            Task created = dao.create(new Task(incoming.getTitle(), incoming.isCompleted()));
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setHeader("Location", req.getContextPath() + req.getServletPath() + "/" + created.getId());
            resp.setContentType("application/json");
            mapper.writeValue(resp.getOutputStream(), created);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(path.replaceFirst("/", ""));
            // load existing task first so we don't clobber title with null
            var existingOpt = dao.findById(id);
            if (existingOpt.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            Task existing = existingOpt.get();

            String compParam = req.getParameter("completed");
            if (compParam != null) {
                existing.setCompleted(Boolean.parseBoolean(compParam));
            } else {
                Task incoming = mapper.readValue(req.getInputStream(), Task.class);
                if (incoming.getTitle() != null) {
                    existing.setTitle(incoming.getTitle());
                }
                existing.setCompleted(incoming.isCompleted());
            }

            boolean ok = dao.update(existing);
            if (ok) resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            else resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException nf) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(path.replaceFirst("/", ""));
            boolean deleted = dao.delete(id);
            if (deleted) resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            else resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (NumberFormatException nf) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
