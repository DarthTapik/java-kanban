package util;

public class Node<E> {
    private E data;
    private Node<E> next;
    private Node<E> prev;

    public Node(E data){
        this.data = data;
    }

    public Node(Node<E> prev, E data, Node<E> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public void setPrev(Node<E> prev) {
        this.prev = prev;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public Node<E> getNext() {
        return next;
    }

    public Node<E> getPrev() {
        return prev;
    }

    public E getData() {
        return data;
    }
}
