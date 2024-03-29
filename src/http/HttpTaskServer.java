package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateTimeAdapter;
import http.handler.*;
import manager.FileBackedTaskManager;

import manager.TaskManager;
import manager.exceptions.ManagerLoadException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    Gson gson;
    HttpServer taskServer;

    public HttpTaskServer(TaskManager manager) throws IOException {
        taskServer = HttpServer.create(new InetSocketAddress(8080), 0);

        LocalDateTimeAdapter localDateTimeAdapter = new LocalDateTimeAdapter();
        DurationAdapter durationAdapter = new DurationAdapter();

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
                .registerTypeAdapter(Duration.class, durationAdapter)
                .serializeNulls()
                .create();

        TasksHandler tasksHandler = new TasksHandler(manager, gson);
        SubTasksHandler subTasksHandler = new SubTasksHandler(manager, gson);
        EpicsHandler epicsHandler = new EpicsHandler(manager, gson);
        HistoryHandler historyHandler = new HistoryHandler(manager, gson);
        PrioritizedHandler prioritizedHandler = new PrioritizedHandler(manager, gson);

        taskServer.createContext("/tasks", tasksHandler);
        taskServer.createContext("/subtasks", subTasksHandler);
        taskServer.createContext("/epics", epicsHandler);
        taskServer.createContext("/history", historyHandler);
        taskServer.createContext("/prioritized", prioritizedHandler);
        taskServer.start();
    }

    public void stop() {
        taskServer.stop(0);
    }

    public static void main(String[] args) {
        Path path = Path.of("src/resources/task.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(path.toFile());
        try {
            manager.loadFromFile(path.toFile());
        } catch (ManagerLoadException e) {
            throw new RuntimeException(e);
        }
        try {
            HttpTaskServer taskServer = new HttpTaskServer(manager);
            System.out.println(manager.getAllTask());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
