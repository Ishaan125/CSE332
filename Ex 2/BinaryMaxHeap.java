import java.lang.reflect.Array;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class BinaryMaxHeap <T extends Comparable<T>> implements MyPriorityQueue<T> {    
    private int size; // Maintains the size of the data structure
    private T[] arr; // The array containing all items in the data structure
                     // index 0 must be utilized
    private Map<T, Integer> itemToIndex; // Keeps track of which index of arr holds each item.

    public BinaryMaxHeap(){
        // This line just creates an array of type T. We're doing it this way just
        // because of weird java generics stuff (that I frankly don't totally understand)
        // If you want to create a new array anywhere else (e.g. to resize) then
        // You should mimic this line. The second argument is the size of the new array.
        arr = (T[]) Array.newInstance(Comparable.class, 10);
        size = 0;
        itemToIndex = new HashMap<>();
    }

    // move the item at index i "rootward" until
    // the heap property holds
    private void percolateUp(int i) {
        int parent = (i-1)/2;
        while (parent >= 0 && arr[parent].compareTo(arr[i]) < 0) {
            T temp = arr[i];
            arr[i] = arr[parent];
            arr[parent] = temp;
            
            itemToIndex.put(arr[parent], parent);
            itemToIndex.put(arr[i], i);
            i = parent;
            parent = (i-1)/2;
        }
    }

    // move the item at index i "leafward" until
    // the heap property holds
    private void percolateDown(int i) {
        int child = 2*i + 1;
        while (child < size && ((arr[child].compareTo(arr[i]) > 0) || (child + 1 < size && arr[child + 1].compareTo(arr[i]) > 0))) {
            if (child + 1 < size && arr[child + 1].compareTo(arr[child]) > 0) {
                child++; // swap to right child if it's larger
            }
            T temp = arr[i];
            arr[i] = arr[child];
            arr[child] = temp;
            itemToIndex.put(arr[child], child);
            itemToIndex.put(arr[i], i);
            i = child;
            child = 2*i + 1;
        }
    }

    // copy all items into a larger array to make more room.
    private void resize(){
        T[] larger = (T[]) Array.newInstance(Comparable.class, arr.length*2);
        for(int i = 0; i < arr.length; i++){
            larger[i] = arr[i];
        }
        arr = larger;
    }

    // Add the given item to the priority queue.
    public void insert(T item){
        if (size == arr.length) {
            resize();
        }
        arr[size] = item;
        itemToIndex.put(item, size);
        size++;
        percolateUp(size - 1);
    }

    // Remove and return the top priority item.
    public T extract() {
        if (size == 0) {
            throw new IllegalStateException();
        }

        T extracted = arr[0];
        arr[0] = arr[size - 1];
        arr[size - 1] = null;
        itemToIndex.remove(extracted);
        size--;

        if (arr[0] == null) { // size = 1 before extraction
            return extracted;
        }

        itemToIndex.put(arr[0], 0);
        percolateDown(0);
        return extracted;
    }

    // Remove the item at the given index.
    // Make sure to maintain the heap property!
    private T remove(int index) {
        if (size == 0 || index < 0 || index >= size) {
            throw new IllegalArgumentException();
        }

        T extracted = arr[index];
        itemToIndex.remove(extracted);

        if (index == size - 1) { // removing the last item in the heap
            arr[index] = null;
            size--;
            return extracted;
        }

        arr[index] = arr[size - 1];
        arr[size - 1] = null;
        itemToIndex.put(arr[index], index);
        size--;
        updatePriority(index);
        return extracted;
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public void remove(T item){
        if (!itemToIndex.containsKey(item)){
            throw new IllegalArgumentException();
        }
        remove(itemToIndex.get(item));
    }

    // Determine whether to percolate up/down
    // the item at the given index, then do it!
    private void updatePriority(int index) {
        if (index >= 0 && index < size && arr[(index-1)/2].compareTo(arr[index]) < 0) {
            percolateUp(index);
        }
        else if (index >= 0 && index < size) {
            percolateDown(index);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    // This method gets called after the client has 
    // changed an item in a way that may change its
    // priority. In this case, the client should call
    // updatePriority on that changed item so that 
    // the heap can restore the heap property.
    // Throws an IllegalArgumentException if the given
    // item is not an element of the priority queue.
    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public void updatePriority(T item){
	    if(!itemToIndex.containsKey(item)){
            throw new IllegalArgumentException("Given item is not present in the priority queue!");
	    }
        updatePriority(itemToIndex.get(item));
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public boolean isEmpty(){
        return size == 0;
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public int size(){
        return size;
    }

    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public T peek(){
        if(isEmpty()){
            throw new IllegalStateException();
        }
        return arr[0];
    }
    
    // We have provided a recommended implementation
    // You're welcome to do something different, though!
    public List<T> toList(){
        List<T> copy = new ArrayList<>();
        for(int i = 0; i < size; i++){
            copy.add(i, arr[i]);
        }
        return copy;
    }

    // For debugging
    public String toString(){
        if(size == 0){
            return "[]";
        }
        String str = "[(" + arr[0] + " " + itemToIndex.get(arr[0]) + ")";
        for(int i = 1; i < size; i++ ){
            str += ",(" + arr[i] + " " + itemToIndex.get(arr[i]) + ")";
        }
        return str + "]";
    }   
}
