import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class TopKHeap<T extends Comparable<T>> {
    private BinaryMinHeap<T> topK; // Holds the top k items
    private BinaryMaxHeap<T> rest; // Holds all items other than the top k
    private final int k; // The value of k
    private Map<T, MyPriorityQueue<T>> itemToHeap; // Keeps track of which heap contains each item.
    
    // Creates a topKHeap for the given choice of k.
    public TopKHeap(int k){
        topK = new BinaryMinHeap<>();
        rest = new BinaryMaxHeap<>();
        this.k = k;
        itemToHeap = new HashMap<>();
    }

    // Returns a list containing exactly the
    // largest k items. The list is not necessarily
    // sorted. If the size is less than or equal to
    // k then the list will contain all items.
    // The running time of this method should be O(k).
    public List<T> topK() {
        return topK.toList();
    }

    // Add the given item into the data structure.
    // The running time of this method should be O(log(n)+log(k)).
    public void insert(T item) {
        if (topK.size() < k) { // topK isn't full yet, so just add the new item there
            topK.insert(item);
            itemToHeap.put(item, topK);
        }
        else if (item.compareTo(topK.peek()) > 0) { // swap between topK and rest
            T temp = topK.extract();
            rest.insert(temp);
            topK.insert(item);
            itemToHeap.put(item, topK);
            itemToHeap.put(temp, rest);
        }
        else { // item belongs in rest
            rest.insert(item);
            itemToHeap.put(item, rest);
        }
    }

    // Indicates whether the given item is among the 
    // top k items. Should return false if the item
    // is not present in the data structure at all.
    // The running time of this method should be O(1).
    // We have provided a suggested implementation,
    // but you're welcome to do something different!
    public boolean isTopK(T item){
        return itemToHeap.get(item) == topK;
    }

    // To be used whenever an item's priority has changed.
    // The input is a reference to the items whose priority
    // has changed. This operation will then rearrange
    // the items in the data structure to ensure it
    // operates correctly.
    // Throws an IllegalArgumentException if the item is
    // not a member of the heap.
    // The running time of this method should be O(log(n)+log(k)).
    public void updatePriority(T item) {
        if (!itemToHeap.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        itemToHeap.get(item).updatePriority(item);

        if (itemToHeap.get(item) == rest && item.compareTo(topK.peek()) > 0) { // swap between rest and topK
            T temp = topK.extract();
            rest.remove(item);
            topK.insert(item);
            rest.insert(temp);
            itemToHeap.put(item, topK);
            itemToHeap.put(temp, rest);
        }
        else if (itemToHeap.get(item) == topK && !rest.isEmpty() && item.compareTo(rest.peek()) < 0) { // swap between topK and rest
            T temp = rest.extract();
            topK.remove(item);
            rest.insert(item);
            topK.insert(temp);
            itemToHeap.put(item, rest);
            itemToHeap.put(temp, topK);
        }
    }

    // Removes the given item from the data structure
    // throws an IllegalArgumentException if the item
    // is not present.
    // The running time of this method should be O(log(n)+log(k)).
    public void remove(T item) {
        if (!itemToHeap.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        itemToHeap.get(item).remove(item); // remove the item from whichever heap it's in

        if (itemToHeap.get(item) == topK && !rest.isEmpty()) {
            T temp = rest.extract();
            topK.insert(temp);
            itemToHeap.put(temp, topK);
        }
        itemToHeap.remove(item); // remove the item from the map since it's no longer in either heap
    }
}
