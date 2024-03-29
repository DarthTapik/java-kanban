package http.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SubTasksHandler extends Handler {

    enum SubTaskEndpoint {
        GET_SUBTASKS,
        GET_SUBTASK_BY_ID,
        POST_SUBTASK,
        DELETE_SUBTASK,
        UNKNOWN
    }

    private final TaskManager manager;
    private final Gson gson;

    public SubTasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        SubTaskEndpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        System.out.println(endpoint);

        switch (endpoint) {
            case GET_SUBTASKS:
                handleGetSubTasks(exchange);
                break;
            case GET_SUBTASK_BY_ID:
                handleGetSubTaskById(exchange);
                break;
            case POST_SUBTASK:
                handlePostSubTask(exchange);
                break;
            case DELETE_SUBTASK:
                handleDeleteSubTask(exchange);
                break;
            default:
                writeResponse(exchange, notFound, 404);
        }


    }

    private SubTaskEndpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        switch (requestMethod) {
            case "GET":
                if (path.length == 2) {
                    return SubTaskEndpoint.GET_SUBTASKS;
                }
                if (path.length == 3) {
                    return SubTaskEndpoint.GET_SUBTASK_BY_ID;
                }
                break;
            case "POST":
                return SubTaskEndpoint.POST_SUBTASK;
            case "DELETE":
                if (path.length == 3) {
                    return SubTaskEndpoint.DELETE_SUBTASK;
                }
            default:
                return SubTaskEndpoint.UNKNOWN;
        }
        return SubTaskEndpoint.UNKNOWN;
    }

    private void handleGetSubTasks(HttpExchange exchange) throws IOException {
        List<SubTask> subTaskList = manager.getAllSubTask();
        writeResponse(exchange, gson.toJson(subTaskList), 200);
    }

    private void handleGetSubTaskById(HttpExchange exchange) throws IOException {
        Optional<Integer> optionalSubTaskId = getSubTaskId(exchange);
        if (optionalSubTaskId.isEmpty()) {
            writeResponse(exchange, badRequest, 400);
            return;
        }
        int subTaskId = optionalSubTaskId.get();
        if (manager.getSubTask(subTaskId) == null) {
            writeResponse(exchange, notFound, 404);
            return;
        }
        Task task = manager.getSubTask(subTaskId);
        writeResponse(exchange, gson.toJson(task), 200);
    }

    private void handlePostSubTask(HttpExchange exchange) throws IOException {
        Optional<SubTask> optionalSubTask = parseSubTask(exchange);
        if (optionalSubTask.isEmpty()) {
            writeResponse(exchange, badRequest, 400);
            return;
        }
        SubTask subTask = optionalSubTask.get();
        int subTaskId = subTask.getId();
        if (subTaskId > 0) {
            if (manager.getSubTask(subTaskId) != null) {
                manager.updateSubTask(subTask);
                writeResponse(exchange, "", 201);
            } else {
                writeResponse(exchange, notFound, 404);
            }

        } else {
            manager.addSubTask(subTask);
            if (!manager.getAllSubTask().contains(subTask)) {
                writeResponse(exchange, notAcceptable, 406);
                return;
            }
            writeResponse(exchange, "", 201);
        }
    }

    private void handleDeleteSubTask(HttpExchange exchange) throws IOException {
        Optional<Integer> optionalSubTaskId = getSubTaskId(exchange);
        if (optionalSubTaskId.isEmpty()) {
            writeResponse(exchange, badRequest, 400);
            return;
        }
        int subTaskId = optionalSubTaskId.get();
        if (manager.getSubTask(subTaskId) == null) {
            writeResponse(exchange, notFound, 404);
            return;
        }

        manager.deleteSubTask(subTaskId);
        if (manager.getTask(subTaskId) == null) {
            writeResponse(exchange, "", 200);
        }
    }

    private Optional<Integer> getSubTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private Optional<SubTask> parseSubTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (jsonElement.isJsonObject()) {
            SubTask subTask = gson.fromJson(jsonElement, SubTask.class);
            return Optional.of(subTask);
        } else {
            return Optional.empty();
        }
    }
}
