public class ArrayQueue<T> implements MyQueue<T>{
    // We've provided you several data fields to implement the circular array
    // Feel free to add some more if you need!
    // Please do not change the visibility of [data].
    public T[] data; // store the data
    private int first; // index of the head of the queue
    private int last; // index of the tail of the queue
    private int size; // size of the circular array

    // We've provided you a default implementation of constructor for the ArrayQueue
    public ArrayQueue(){
        this.data = (T[]) new Object[10]; // DO NOT CHANGE INITIAL SIZE HERE
        first = 0;
        last = 0;
        size = 0;
    }

    // Adds an item into the queue
    public void enqueue(T item) {
        if (last >= data.length) {
            last = 0;
        }
        if (size == data.length) {
            T[] arr2 = (T[]) new Object[data.length * 2];
            // copy from first to end of data, then from start of data to before first
            System.arraycopy(data, first, arr2, 0, data.length - first);
            System.arraycopy(data, 0, arr2, data.length - first, first);
            data = arr2;
            first = 0;
            last = size;
        }
        data[last] = item;
        last++;
        size++;
    }

    // Removes and returns the least recently added item from the queue
    // throws an IllegalStateException if the queue is empty
    public T dequeue() {
        if (size == 0) {
            throw new IllegalStateException();
        }
        T res = data[first];
        first++;
        size--;
        if (first >= data.length) {
            first = 0;
        }
        return res;
    }

    // Returns the least recently added item in the queue
    // throws an IllegalStateException if the queue is empty
    public T peek() {
        if (size == 0) {
            throw new IllegalStateException();
        }
        return data[first];
    }

    // Returns the number of items in the queue
    public int size() {
        return size;
    }

    // Returns a boolean indicating whether the queue has items
    public boolean isEmpty() {
        return size == 0;
    }
}
