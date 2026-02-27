package com.mywebapp.todo;

import java.util.List;
import java.util.Optional;

public interface TaskDAO {
    List<Task> findAll() throws Exception;
    Optional<Task> findById(int id) throws Exception;
    Task create(Task task) throws Exception;
    boolean update(Task task) throws Exception;
    boolean delete(int id) throws Exception;
}
