public class ArrayStack<T> implements MyStack<T> {
    private T[] arr;
    private int index;

    // Constructor for ArrayStack
    public ArrayStack(){
        arr = (T[]) new Object[10];
        index = 0;
    }

    // Adds an item into the stack
    public void push(T item) {
        if (index >= arr.length) {
            T[] arr2 = (T[]) new Object[arr.length * 2];
            System.arraycopy(arr, 0, arr2, 0, arr.length);
            arr = arr2;
        }
        arr[index] = item;
        index++;
    }

    // Removes and returns the most recently added item from the stack
    // throws an IllegalStateException if the stack is empty
    public T pop() {
        if (index == 0) {
            throw new IllegalStateException();
        }
        index--;
        return arr[index];
    }

    // Returns the most recently added item in the stack
    // throws an IllegalStateException if the stack is empty
    public T peek() {
        if (index == 0) {
            throw new IllegalStateException();
        }
        return arr[index - 1];
    }

    // Returns the number of items in the stack
    public int size() {
        return index;
    }

    // Returns a boolean indicating whether the stack has items
    public boolean isEmpty() {
        return index == 0;
    }
}
