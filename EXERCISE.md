# 📝 Enunciado – Examen / Práctica Docker  
## Arquitectura Asíncrona Multicontenedor

---

## 📌 Contexto

Se proporciona un proyecto con el código fuente completo de una aplicación distribuida, pero **los siguientes ficheros están vacíos**:

- `api/Dockerfile`
- `worker/Dockerfile`
- `frontend/Dockerfile`
- `docker-compose.yml`

Tu objetivo es **completar estos ficheros** para que la aplicación funcione correctamente utilizando **Docker y Docker Compose**.

---

## 🧠 Descripción del sistema

El sistema implementa un **procesamiento asíncrono de tareas** con la siguiente lógica:

1. El usuario accede a una interfaz web.
2. Desde la interfaz se crean tareas.
3. Las tareas se almacenan en una base de datos.
4. Las tareas se envían a una cola.
5. Un proceso independiente las procesa de forma asíncrona.
6. El estado de las tareas se actualiza y puede consultarse.

---

## 🧱 Componentes del sistema

### 🔹 Frontend
- Servidor web estático
- Sirve HTML y JavaScript
- Consume una API REST
- No accede directamente a la base de datos

### 🔹 API Backend
- Aplicación Java con Spring Boot
- Arquitectura MVC
- Usa JPA para persistencia
- Inserta tareas en una cola Redis
- Expone endpoints REST

### 🔹 Worker
- Aplicación en Python
- Proceso en background
- Consume tareas desde Redis
- Actualiza el estado en MySQL
- No expone ningún puerto

### 🔹 Redis
- Actúa como cola de mensajes
- Comunicación interna entre contenedores

### 🔹 MySQL
- Base de datos relacional
- Persistencia del estado de las tareas
- Debe mantener los datos entre reinicios

---

## 📁 Estructura del proyecto

```text
practica2/
│
├── api/
│   ├── Dockerfile   (vacío)
│   ├── pom.xml
│   └── src/
│       └── main/
│           └── ...
│
├── worker/
│   ├── Dockerfile   (vacío)
│   ├── requirements.txt
│   └── worker.py
│
├── frontend/
│   ├── Dockerfile   (vacío)
│   ├── nginx.conf
│   └── html/
│       └── index.html
│
├── docker-compose.yml   (vacío)
├── .env
├── EXERCISE.md
└── README.md
```

## 🧩 Tareas a realizar
### 1️⃣ Dockerfile del Backend (api/)

Debes:
- Crear una imagen para una aplicación Java .  Tienes que usar un sistema multistage, un stage para la build y otro para la ejecución
- Usa la imagen `maven:3.9-eclipse-temurin-17` para la build y busca una imagen de docker oficial para la ejecución que sea válida para eclipse jre-17 (tag jre-17)
- Compilar el proyecto usando Maven teniendo en cuenta el fichero de dependencias pom.xml (mvn dependency:go-offline)
- Generar genera un paquete .jar con el comando `mvn package -DskipTests`
- Exponer el puerto necesario para la API (puerto 8080)
- Ejecutar la aplicación como proceso principal con el comando `java -jar app.jar`

### 2️⃣ Dockerfile del Worker (worker/)

Debes:
- Crear una imagen para Python (python:3.11-slim)
- Define un directorio de trabajo
- Instalar las dependencias indicadas en requirements.txt (pip install --no-cache-dir -r requirements.txt)
- Copiar el código del worker
- Ejecutar el proceso principal (python worker.py)

No exponer puertos

### 3️⃣ Dockerfile del Frontend (frontend/)

Debes:
- Crear una imagen para servir contenido estático
- Copiar los ficheros HTML (/usr/share/nginx/html)
- Configurar el servidor web (el fichero de configuración debe ir a /etc/nginx/conf.d/default.conf)
- Exponer el puerto adecuado

### 4️⃣ docker-compose.yml

Debes definir los siguientes servicios:

### 🔹mysql:
- Imagen `mysql:8` 
- Variables de entorno MYSQL_DATABASE:  MYSQL_USER:  MYSQL_PASSWORD:   MYSQL_ROOT_PASSWORD: 

### 🔹redis:
- Imagen `redis:7`
- Tiene que lanzar el comando `redis-server`

### 🔹worker
- Sección que monte la imagen
- Depende de mysql y redis

### 🔹api
- Sección que monte la imagen
- Puertos
- Depende de mysql y redis

### 🔹frontend
- Sección que monte la imagen
- Puertos
- Depende de api



El fichero debe:
- Construir las imágenes necesarias
- Definir las dependencias entre servicios
- Exponer únicamente los puertos necesarios
- Usar volúmenes para la base de datos (opcional)
- Cargar variables desde el fichero .env (opcional)

### 🔍 Requisitos funcionales

La aplicación se considerará correcta si:
- El frontend es accesible desde el navegador
- Se pueden crear tareas
- El estado de las tareas cambia automáticamente
- El worker procesa tareas de forma asíncrona
- Los datos persisten tras reiniciar los contenedores

### 📌 Restricciones

- No modificar el código fuente proporcionado
- No usar imágenes innecesarias
- No exponer puertos innecesarios
- No ejecutar más de un proceso por contenedor

### 🎓 Criterios de evaluación

Se evaluará:
- Configuración correcta de ficheros Dockerfile
- Configuración correcta de Docker Compose
- Documentación y explicación de la configuración del escenario docker