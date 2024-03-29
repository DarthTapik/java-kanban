package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateTimeAdapter;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TasksHandlerTest {
    TaskManager manager;
    HttpTaskServer taskServer;
    HttpClient client = HttpClient.newHttpClient();
    LocalDateTimeAdapter localDateTimeAdapter = new LocalDateTimeAdapter();
    DurationAdapter durationAdapter = new DurationAdapter();
    Gson gson = gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
            .registerTypeAdapter(Duration.class, durationAdapter)
            .serializeNulls()
            .create();

    Task task, task1;


    @BeforeEach
    void beforeEach() {
        task = new Task("Название", "Описание", Duration.ofMinutes(15),
                LocalDateTime.of(2024, Month.MARCH, 20, 6, 0));
        task1 = new Task("Название 1", "Описание 1", Duration.ofMinutes(15),
                LocalDateTime.of(2024, Month.MARCH, 20, 7, 0));
        manager = new InMemoryTaskManager();
        manager.deleteAll();
        try {
            taskServer = new HttpTaskServer(manager);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @AfterEach
    void afterEach() {
        taskServer.stop();
    }

    @Test
    void getTaskHandle() {
        manager.addTask(task);
        manager.addTask(task1);

        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            assertTrue(jsonObject.isJsonArray());
            List tasksList = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
            }.getType());
            assertEquals(2, tasksList);
            assertTrue(tasksList.contains(task));
            assertTrue(tasksList.contains(task1));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void getTaskByIdHandle() {
        manager.addTask(task);
        manager.addTask(task1);
        int taskId = task.getId();
        URI uri = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            assertTrue(jsonObject.isJsonObject());
            Task taskFromJson = gson.fromJson(response.body(), task.getClass());
            assertEquals(task, taskFromJson);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void getTaskByWrongIdHandleCheck() {
        int taskId = 3;
        URI uri = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            assertFalse(jsonObject.isJsonObject());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void postCreateTask() {
        String jsonTask = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        uri = URI.create("http://localhost:8080/tasks" + task.getId());
        request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            assertTrue(jsonObject.isJsonObject());
            Task taskFromJson = gson.fromJson(response.body(), Task.class);
            assertEquals(task, taskFromJson);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void postCreateWrongTask() {
        task.setDuration(Duration.ofDays(2));
        manager.addTask(task);

        String jsonTask = gson.toJson(task1);
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(406, response.statusCode());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    @Test
    void postUpdateTask() {
        manager.addTask(task);
        task.setName("Обновленная задача");
        String jsonTask = gson.toJson(task);
        URI uri = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(jsonTask))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        uri = URI.create("http://localhost:8080/tasks" + task.getId());
        request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            assertTrue(jsonObject.isJsonObject());
            Task taskFromJson = gson.fromJson(response.body(), Task.class);
            assertEquals(task, taskFromJson);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    void deleteTask() {
        manager.addTask(task);
        URI uri = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        uri = URI.create("http://localhost:8080/tasks/" + task.getId());
        request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
            assertFalse(jsonObject.isJsonObject());
            Task taskFromJson = gson.fromJson(response.body(), Task.class);
            assertEquals(null, taskFromJson);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
