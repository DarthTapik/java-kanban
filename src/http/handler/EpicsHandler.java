package http.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class EpicsHandler extends Handler {

    enum EpicEndpoint {
        GET_EPICS,
        GET_EPIC_BY_ID,
        POST_EPIC,
        DELETE_EPIC,
        GET_EPICS_SUBTASKS, UNKNOWN
    }

    private final TaskManager manager;
    private final Gson gson;

    public EpicsHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EpicEndpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());
        System.out.println(endpoint);

        switch (endpoint) {
            case GET_EPICS:
                handleGetEpics(exchange);
                break;
            case GET_EPIC_BY_ID:
                handleGetEpicById(exchange);
                break;
            case POST_EPIC:
                handlePostEpic(exchange);
                break;
            case DELETE_EPIC:
                handleDeleteEpic(exchange);
                break;
            case GET_EPICS_SUBTASKS:
                handleGetEpicsSubtasks(exchange);
                break;
            default:
                writeResponse(exchange, notFound, 404);
        }


    }

    private EpicEndpoint getEndpoint(String requestPath, String requestMethod) {
        String[] path = requestPath.split("/");
        switch (requestMethod) {
            case "GET":
                if (path.length == 2) {
                    return EpicEndpoint.GET_EPICS;
                }
                if (path.length == 3) {
                    return EpicEndpoint.GET_EPIC_BY_ID;
                }
                if (path.length == 4 && path[3].equals("subtasks")) {
                    return EpicEndpoint.GET_EPICS_SUBTASKS;
                } else {
                    return EpicEndpoint.UNKNOWN;
                }
            case "POST":
                return EpicEndpoint.POST_EPIC;
            case "DELETE":
                if (path.length == 3) {
                    return EpicEndpoint.DELETE_EPIC;
                }
            default:
                return EpicEndpoint.UNKNOWN;
        }
    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        List<Epic> epicList = manager.getAllEpic();
        writeResponse(exchange, gson.toJson(epicList), 200);
    }

    private void handleGetEpicById(HttpExchange exchange) throws IOException {
        Optional<Integer> optionalEpicId = getEpicId(exchange);
        if (optionalEpicId.isEmpty()) {
            writeResponse(exchange, badRequest, 400);
            return;
        }
        int epicId = optionalEpicId.get();
        if (manager.getEpic(epicId) == null) {
            writeResponse(exchange, notFound, 404);
            return;
        }
        Epic epic = manager.getEpic(epicId);
        writeResponse(exchange, gson.toJson(epic), 200);
    }

    private void handlePostEpic(HttpExchange exchange) throws IOException {
        Optional<Epic> optionalEpic = parseEpic(exchange);
        if (optionalEpic.isEmpty()) {
            writeResponse(exchange, badRequest, 400);
            return;
        }
        Epic epic = optionalEpic.get();
        int epicId = epic.getId();
        if (epicId > 0) {
            if (manager.getAllEpic().contains(manager.getEpic(epicId))) {
                manager.updateEpic(epic);
                writeResponse(exchange, "", 201);
            } else {
                writeResponse(exchange, notFound, 404);
            }

        } else {
            manager.addEpic(epic);
            if (!manager.getAllEpic().contains(epic)) {
                writeResponse(exchange, notAcceptable, 406);
                return;
            }
            writeResponse(exchange, "", 201);
        }
    }

    private void handleDeleteEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> optionalEpicId = getEpicId(exchange);
        if (optionalEpicId.isEmpty()) {
            writeResponse(exchange, badRequest, 400);
            return;
        }
        int epicId = optionalEpicId.get();
        if (manager.getEpic(epicId) == null) {
            writeResponse(exchange, notFound, 404);
            return;
        }

        manager.deleteEpic(epicId);
        if (manager.getEpic(epicId) == null) {
            writeResponse(exchange, "", 200);
        }
    }

    private void handleGetEpicsSubtasks(HttpExchange exchange) throws IOException {
        Optional<Integer> optionalEpicId = getEpicId(exchange);
        if (optionalEpicId.isEmpty()) {
            writeResponse(exchange, badRequest, 400);
            return;
        }
        int epicId = optionalEpicId.get();
        if (manager.getEpic(epicId) == null) {
            writeResponse(exchange, notFound, 404);
            return;
        }
        Epic epic = manager.getEpic(epicId);
        writeResponse(exchange, gson.toJson(epic.getSubTaskList()), 200);
    }

    private Optional<Integer> getEpicId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private Optional<Epic> parseEpic(HttpExchange exchange) throws IOException {
        String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
        JsonElement jsonElement = JsonParser.parseString(body);
        if (jsonElement.isJsonObject()) {
            Epic epic = gson.fromJson(jsonElement, Epic.class);
            return Optional.of(epic);
        } else {
            return Optional.empty();
        }
    }


}
