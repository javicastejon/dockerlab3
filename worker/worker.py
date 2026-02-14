import time
import os
import redis
import mysql.connector

# Conexión a Redis (cola de tareas)
redis_client = redis.Redis(
    host="redis",
    port=6379,
    decode_responses=True
)

# Conexión a MySQL
mysql_conn = mysql.connector.connect(
    host="mysql",
    user="usuario",
    password="secreto",
    database="ejemplo"
)

cursor = mysql_conn.cursor()

print("Worker iniciado. Esperando tareas...")

while True:
    # Espera bloqueante a una tarea en la cola
    tarea = redis_client.blpop("cola_tareas", timeout=5)

    if tarea:
        task_id = tarea[1]
        print(f"Procesando tarea {task_id}")

        # Marcar como PROCESANDO
        cursor.execute(
            "UPDATE tasks SET estado = 'PROCESANDO' WHERE id = %s",
            (task_id,)
        )
        mysql_conn.commit()

        # Simulación de trabajo pesado
        time.sleep(5)

        # Marcar como COMPLETADA
        cursor.execute(
            "UPDATE tasks SET estado = 'COMPLETADA' WHERE id = %s",
            (task_id,)
        )
        mysql_conn.commit()