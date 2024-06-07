package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.InMemoryTaskManager;
import manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends Handler {

    private final TaskManager manager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager;
        writeResponse(exchange, gson.toJson(inMemoryTaskManager.getPrioritizedTasks()), 200);
    }
}