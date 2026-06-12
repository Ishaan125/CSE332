import java.util.*;

public class NaiveQueue<T> implements MyQueue<T>{
    private List<T> queue;

    public NaiveQueue(){
        // Initialize an ArrayList with capacity of 10,000
        // Do not change this value, as it might affect benchmarking results
        this.queue = new ArrayList<>(10000);
    }

    public int size(){
        return this.queue.size();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public T dequeue(){
        return queue.remove(0);
    }

    public void enqueue(T item){
        queue.add(item);
    }

    public T peek(){
        return queue.get(0);
    }
}
