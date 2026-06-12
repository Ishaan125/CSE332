public class LinkedQueue<E> implements MyQueue<E>{
    private ListNode<E> head;
    private ListNode<E> tail;
    private int size;

    // Constructor for LinkedQueue
    public LinkedQueue(){
        head = null;
        tail = head;
        size = 0;
    }

    // Adds an item into the queue
    public void enqueue(E item) {
        if (head == null) {
            head = new ListNode<>(item);
            tail = head;
        } else {
            tail.next = new ListNode<>(item);
            tail = tail.next;
        }
        size++;
    }

    // Removes and returns the least recently added item from the queue
    // Throws an IllegalStateException if the queue is empty
    public E dequeue() {
        if (head == null) {
            throw new IllegalStateException();
        }
        E item = head.data;
        head = head.next;
        size--;
        return item;
    }

    // Removes and returns the least recently added item from the queue
    // throws an IllegalStateException if the queue is empty
    public E peek() {
        if (head == null) {
            throw new IllegalStateException();
        }
        return head.data;
    }

    // Returns the number of items in the queue
    public int size() {
        return size;
    }

    // Returns a boolean indicating whether the queue has items
    public boolean isEmpty() {
        return head == null;
    }

    private static class ListNode<E>{
        private final E data;
        private ListNode<E> next;

        private ListNode(E data, ListNode<E> next){
            this.data = data;
            this.next = next;
        }

        private ListNode(E data){
            this.data = data;
        }
    }
}
