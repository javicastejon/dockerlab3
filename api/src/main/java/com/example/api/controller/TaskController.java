package com.example.api.controller;

import com.example.api.model.Task;
import com.example.api.repository.TaskRepository;
import com.example.api.service.QueueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {

    private final TaskRepository repo;
    private final QueueService queue;

    public TaskController(TaskRepository repo, QueueService queue) {
        this.repo = repo;
        this.queue = queue;
    }

    @GetMapping
    public List<Task> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Task crear(@RequestBody Task task) {
        task.setEstado("PENDIENTE");
        Task guardada = repo.save(task);
        queue.enviarTarea(guardada.getId());
        return guardada;
    }
}