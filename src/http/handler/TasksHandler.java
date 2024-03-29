package http.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TasksHandler extends Handler {

    enum TaskEndpoint{
        GET_TASKS,
        GET_TASK_BY_ID,
        POST_TASK,
        DELETE_TASK,
        UNKNOWN
    }

    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager, Gson gson){
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        TaskEndpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        System.out.println(endpoint);

        switch (endpoint){
            case GET_TASKS:
                handleGetTasks(exchange);
                break;
            case GET_TASK_BY_ID:
                handleGetTaskById(exchange);
                break;
            case POST_TASK:
                handlePostTask(exchange);
                break;
            case DELETE_TASK:
                handleDeleteTask(exchange);
                break;
            default:
                writeResponse(exchange,notFound, 404);
        }


    }

    private TaskEndpoint getEndpoint(String requestPath, String requestMethod){
        String[] path = requestPath.split("/");
        switch (requestMethod) {
            case "GET":
                if (path.length == 2) {
                    return TaskEndpoint.GET_TASKS;
                }
                if (path.length == 3) {
                    return TaskEndpoint.GET_TASK_BY_ID;
                }
                break;
            case "POST":
                return TaskEndpoint.POST_TASK;
            case "DELETE":
                if (path.length == 3) {
                    return TaskEndpoint.DELETE_TASK;
                }
            default:
                return TaskEndpoint.UNKNOWN;
        }
        return TaskEndpoint.UNKNOWN;
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
       List<Task> taskList = manager.getAllTask();
       writeResponse(exchange, gson.toJson(taskList), 200);
    }

    private void handleGetTaskById(HttpExchange exchange) throws IOException{
        Optional<Integer> optionalTaskId = getTaskId(exchange);
        if (optionalTaskId.isEmpty()){
            writeResponse(exchange, badRequest, 400);
            return;
        }
        int taskId = optionalTaskId.get();
        if (manager.getTask(taskId) == null){
            writeResponse(exchange, notFound, 404);
            return;
        }
        Task task = manager.getTask(taskId);
        writeResponse(exchange, gson.toJson(task), 200);
    }

    private void handlePostTask(HttpExchange exchange) throws  IOException{
        Optional<Task> optionalTask = parseTask(exchange);
        if (optionalTask.isEmpty()){
            writeResponse(exchange, badRequest, 400);
            return;
        }
        Task task = optionalTask.get();
        int taskId = task.getId();
        if (taskId > 0){
            if (manager.getAllTask().contains(manager.getTask(taskId))){
                manager.updateTask(task);
                writeResponse(exchange, "", 201);
            } else {
                writeResponse(exchange, notFound, 404);
            }

        } else {
            manager.addTask(task);
            if (!manager.getAllTask().contains(task)){
                writeResponse(exchange,notAcceptable, 406);
                return;
            }
            writeResponse(exchange, "", 201);
        }
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        Optional<Integer> optionalTaskId = getTaskId(exchange);
        if (optionalTaskId.isEmpty()){
            writeResponse(exchange, badRequest, 400);
            return;
        }
        int taskId = optionalTaskId.get();
        if (manager.getTask(taskId) == null){
            writeResponse(exchange,notFound, 404);
            return;
        }

        manager.deleteTask(taskId);
        if (manager.getTask(taskId) == null){
            writeResponse(exchange, "", 200);
        }
    }

    private Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private Optional<Task> parseTask(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (jsonElement.isJsonObject()){
            Task task = gson.fromJson(jsonElement, Task.class);
            return Optional.of(task);
        } else {
            return Optional.empty();
        }
    }




}
