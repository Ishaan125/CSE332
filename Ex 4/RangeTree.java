import java.util.List;

public class RangeTree {
    private OrderedDeletelessDictionary<Double, Range> byStart;
    private OrderedDeletelessDictionary<Double, Range> byEnd;
    private int size;

    public RangeTree() {
        byStart = new AVLTree<>();
        byEnd = new AVLTree<>();
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Return the Range which starts at the given time
    // The running time is O(log n)
    public Range findByStart(Double start) {
        return byStart.find(start);
    }

    // Return the Range which ends at the given time
    // The running time is O(log n)
    public Range findByEnd(Double end) {
        return byEnd.find(end);
    }

    // Gives a list of Ranges sorted by start time.
    // Useful for testing and debugging.
    // The running time is O(n), so it should not
    // be used for implementing other methods.
    public List<Range> getRanges() {
        return byStart.getValues();
    }

    // Gives a sorted list of start times.
    // Useful for testing and debugging.
    // The running time is O(n), so it should not
    // be used for implementing other methods.
    public List<Double> getStartTimes() {
        return byStart.getKeys();
    }

    // Gives a sorted list of end times.
    // Useful for testing and debugging.
    // The running time is O(n), so it should not
    // be used for implementing other methods.
    public List<Double> getEndTimes() {
        return byEnd.getKeys();
    }

    // Identifies whether or not the given range conflicts with any
    // ranges that are already in the data structure.
    // If the data structure is empty, then it does not conflict
    // with any ranges, so we should return false.
    // If one event ends at the same time another event starts,
    // then they do not conflict.
    // The data structure will only hold non-conflicting ranges.
    // The running time of this method should be O(log n)
    public boolean hasConflict(Range query) {
        if (isEmpty()) {
            return false;
        }

        // Equal boundaries = overlap with an existing range
        if (byStart.find(query.start) != null || byEnd.find(query.end) != null) {
            return true;
        }

        Double prevStart = byStart.findPrevKey(query.start);
        Double nextStart = byStart.findNextKey(query.start);
        return (prevStart != null && byStart.find(prevStart).end > query.start) || (nextStart != null && byStart.find(nextStart).start < query.end);
    }

    // Inserts the given range into the data structure if it has no conflict.
    // Does not modify the data structure if it does have a conflict.
    // Return value indicates whether or not the item was successfully
    // added to the data structure.
    // Running time should be O(log n)
    public boolean insert(Range query) {
        if (hasConflict(query)) {
            return false;
        }

        byStart.insert(query.start, query);
        byEnd.insert(query.end, query);
        size++;
        return true;
    }
}
