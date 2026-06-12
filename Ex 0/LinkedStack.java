public class LinkedStack<T> implements MyStack<T> {
    private ListNode<T> head;
    private int size;

    // Constructor for LinkedStack
    public LinkedStack(){
        head = null;
        size = 0;
    }

    // Adds an item into the stack
    public void push(T item) {
        ListNode<T> newNode = new ListNode<>(item);
        newNode.next = head;
        head = newNode;
        size++;
    }

    // Removes and returns the most recently added item from the stack
    // throws an IllegalStateException if the stack is empty
    public T pop() {
        if (head == null) {
            throw new IllegalStateException();
        }
        T item = head.data;
        head = head.next;
        size--;
        return item;
    }

    // Returns the most recently added item in the stack
    // throws an IllegalStateException if the stack is empty
    public T peek() {
        if (head == null) {
            throw new IllegalStateException();
        }
        return head.data;
    }

    // Returns the number of items in the stack
    public int size() {
        return size;
    }

    // Returns a boolean indicating whether the stack has items
    public boolean isEmpty() {
        return head == null;
    }

    private static class ListNode<T> {
        private final T data;
        private ListNode<T> next;

        private ListNode(T data, ListNode<T> next){
            this.data = data;
            this.next = next;
        }

        private ListNode(T data){
            this.data = data;
        }
    }
}
