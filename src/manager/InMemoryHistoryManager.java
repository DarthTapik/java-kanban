package manager;

import tasks.Task;
import util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {


    private HashMap<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> historyList = new ArrayList<>();
        Node<Task> prev = tail;
        while (prev != null) {
            historyList.add(prev.getData());
            prev = prev.getPrev();
        }
        return historyList;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);


    }

    public void linkLast(Task task) {
        Node<Task> node;
        if (history.containsKey(task.getId())) {
            removeNode(history.get(task.getId()));
        }
        if (history.isEmpty()) {
            node = new Node(task);
            head = node;
        } else {
            node = new Node(tail, task, null);
            tail.setNext(node);
        }
        tail = node;

        history.put(task.getId(), node);
    }

    public void removeNode(Node<Task> node) {
        if (head == null || node == null) {
            return;
        }

        // Если удаляемый узел — первый узел
        if (head == node) {
            head = node.getNext();
        }

        // Если удаляемый узел — последний узел
        if (tail == node) {
            tail = node.getPrev();
        }

        // Изменение указателей соседних узлов
        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        }

        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        }
        history.remove(node.getData().getId());
    }

}
