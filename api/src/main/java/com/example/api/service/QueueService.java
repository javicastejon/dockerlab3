package com.example.api.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

    private final StringRedisTemplate redis;

    public QueueService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void enviarTarea(Long taskId) {
        redis.opsForList().rightPush("cola_tareas", taskId.toString());
    }
}