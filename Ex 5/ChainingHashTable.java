import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChainingHashTable<K, V> implements DeletelessDictionary<K, V> {
    public List<Item<K, V>>[] table; // The table itself is an array of linked lists of items.
    private int size;
    private static int[] primes = {11, 23, 47, 97, 197, 397};

    public ChainingHashTable() {
        table = (LinkedList<Item<K, V>>[]) Array.newInstance(LinkedList.class, primes[0]);
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
    }

    // copy items to larger table and rehash
    private void rehash() {
        int len = table.length;
        int idx = -1;
        for (int i = 0; i < primes.length - 1; i++) {
            if (primes[i] == len) {
                idx = i;
                break;
            }
        }

        // pick a new size
        if (idx != -1) {
            len = primes[idx + 1];
        } else {
            int n = (int) (Math.log(len) / Math.log(2)); // change of base formula
            len = (int) Math.pow(2, n + 1) + 1;
        }

        // create new table and copy items
        List<Item<K, V>>[] newTable = (LinkedList<Item<K, V>>[]) Array.newInstance(LinkedList.class, len);
        for (int i = 0; i < newTable.length; i++) {
            newTable[i] = new LinkedList<>();
        }

        for (List<Item<K, V>> list : table) {
            for (Item<K, V> item : list) {
                newTable[Math.abs(item.key.hashCode() % newTable.length)].add(item);
            }
        }
        table = newTable;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // If the key is already in the table, replace the value and return the old value. Otherwise, add a new item and return null.
    public V insert(K key, V value) {
        int i = Math.abs(key.hashCode() % table.length);
        for (Item<K,V> item : table[i]) {
            if (item.key.equals(key)) {
                V oldValue = item.value;
                item.value = value;
                return oldValue;
            }
        }

        table[i].add(new Item<>(key, value));
        size++;
        if (size >= 2 * table.length) {
            rehash();
        }
        return null;
    }

    public V find(K key) {
        int i = Math.abs(key.hashCode() % table.length);
        for (Item<K, V> item : table[i]) {
            if (item.key.equals(key)) {
                return item.value;
            }
        }
        return null;
    }

    public boolean contains(K key) {
        return find(key) != null;
    }

    public List<K> getKeys() {
        List<K> keys = new ArrayList<>();
        for (List<Item<K, V>> list : table) {
            for (Item<K, V> item : list) {
                keys.add(item.key);
            }
        }
        return keys;
    }
    
    public List<V> getValues() {
        List<V> values = new ArrayList<>();
        for (List<Item<K, V>> list : table) {
            for (Item<K, V> item : list) {
                values.add(item.value);
            }
        }
        return values;
    }

    public String toString() {
        String s = "{";
        s += table[0];
        for (int i = 1; i < table.length; i++) {
            s += "," + table[i];
        }
        return s + "}";
    }
}
