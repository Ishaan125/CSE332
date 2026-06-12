import java.util.*;

public class NaiveStack<T> implements MyStack<T> {
    private List<T> stack;

    public NaiveStack(){
        // Initialize an ArrayList with capacity of 10,000
        // Do not change this value, as it might affect benchmarking results
        stack = new ArrayList<T>(10000);
    }

    public int size(){
        return stack.size();
    }

    public boolean isEmpty(){
        return stack.isEmpty();
    }

    public void push(T value){
        stack.add(0, value);
    }

    public T peek(){
        return stack.get(0);
    }

    public T pop(){
        return stack.remove(0);
    }

}
