package manager;

import tasks.Task;
import util.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class InMemoryHistoryManager implements HistoryManager{


    HashMap<Integer, Node<Task>> history = new HashMap<>();
    Node<Task> head;
    Node<Task> tail;

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> historyList = new ArrayList<>();
        Node<Task> next = head;
        while (next != null){
            historyList.add(next.getData());
            next = next.getNext();
        }
        return historyList;
    }

    @Override
    public void add(Task task){
        if (task == null){
            return;
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    public void linkLast(Task task){
        Node<Task> node;
        if (history.isEmpty()){
            node = new Node(task);
            head = node;
        } else {
            node = new Node(tail ,task, null);
            tail.setNext(node);
        }
        tail = node;
        if (history.containsKey(task.getId())){
            removeNode(node);
        }
        history.put(task.getId(), node);
    }

    public void removeNode(Node<Task> node){
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
